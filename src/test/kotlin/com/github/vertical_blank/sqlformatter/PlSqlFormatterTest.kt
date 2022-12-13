package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.features.supportsAlterTable
import com.github.vertical_blank.sqlformatter.features.supportsAlterTableModify
import com.github.vertical_blank.sqlformatter.features.supportsBetween
import com.github.vertical_blank.sqlformatter.features.supportsCase
import com.github.vertical_blank.sqlformatter.features.supportsCreateTable
import com.github.vertical_blank.sqlformatter.features.supportsJoin
import com.github.vertical_blank.sqlformatter.features.supportsOperators
import com.github.vertical_blank.sqlformatter.features.supportsSchema
import com.github.vertical_blank.sqlformatter.features.supportsStrings
import com.github.vertical_blank.sqlformatter.languages.Dialect
import com.github.vertical_blank.sqlformatter.languages.StringLiteral
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PlSqlFormatterTest :
  Spek({
    val formatter = SqlFormatter.of(Dialect.PlSql)

    describe("PlSqlFormatter") {
      with(formatter) {
        behavesLikeSqlFormatter(formatter)
        supportsCase(formatter)
        supportsCreateTable(formatter)
        supportsAlterTable(formatter)
        supportsAlterTableModify(formatter)
        supportsStrings(
          formatter,
          listOf(
            StringLiteral.DOUBLE_QUOTE,
            StringLiteral.SINGLE_QUOTE,
            StringLiteral.BACK_QUOTE,
            StringLiteral.Q_SINGLE_QUOTE
          )
        )
        supportsBetween(formatter)
        supportsSchema(formatter)
        supportsOperators(formatter, listOf("||", "**", "!=", ":="))
        supportsJoin(formatter)

        it("formats FETCH FIRST like LIMIT") {
          expect(format("SELECT col1 FROM tbl ORDER BY col2 DESC FETCH FIRST 20 ROWS ONLY;"))
            .toBe(
              """
        SELECT
          col1
        FROM
          tbl
        ORDER BY
          col2 DESC
        FETCH FIRST
          20 ROWS ONLY;
                    """.trimIndent()
            )
        }

        it("formats only -- as a line comment") {
          val result = format("SELECT col FROM\n-- This is a comment\nMyTable;\n")
          expect(result)
            .toBe(
              """
        SELECT
          col
        FROM
          -- This is a comment
          MyTable;
                    """.trimIndent()
            )
        }

        it("recognizes _, $, #, . and @ as part of identifiers") {
          val result = format("SELECT my_col\$1#, col.2@ FROM tbl\n")
          expect(result)
            .toBe(
              """
        SELECT
          my_col$1#,
          col.2@
        FROM
          tbl
                    """.trimIndent()
            )
        }

        it("formats INSERT without INTO") {
          val result =
            format(
              "INSERT Customers (ID, MoneyBalance, Address, City) VALUES (12,-123.4, 'Skagen 2111','Stv');"
            )
          expect(result)
            .toBe(
              """
        INSERT
          Customers (ID, MoneyBalance, Address, City)
        VALUES
          (12, -123.4, 'Skagen 2111', 'Stv');
                    """.trimIndent()
            )
        }

        it("recognizes ?[0-9]* placeholders") {
          val result = format("SELECT ?1, ?25, ?;")
          expect(result)
            .toBe(
              """
        SELECT
          ?1,
          ?25,
          ?;
                    """.trimIndent()
            )
        }

        it("replaces ? numbered placeholders with param values") {
          val result =
            format(
              "SELECT ?1, ?2, ?0;",
              mapOf(
                "0" to "first",
                "1" to "second",
                "2" to "third",
              )
            )
          expect(result).toBe("SELECT\n" + "  second,\n" + "  third,\n" + "  first;")
        }

        it("replaces ? indexed placeholders with param values") {
          val result = format("SELECT ?, ?, ?;", listOf("first", "second", "third"))
          expect(result).toBe("SELECT\n" + "  first,\n" + "  second,\n" + "  third;")
        }

        it("formats SELECT query with CROSS APPLY") {
          val result = format("SELECT a, b FROM t CROSS APPLY fn(t.id)")
          expect(result)
            .toBe(
              """
        SELECT
          a,
          b
        FROM
          t
          CROSS APPLY fn(t.id)
                    """.trimIndent()
            )
        }

        it("formats simple SELECT") {
          val result = format("SELECT N, M FROM t")
          expect(result)
            .toBe(
              """
        SELECT
          N,
          M
        FROM
          t
                    """.trimIndent()
            )
        }

        it("formats simple SELECT with national characters") {
          val result = format("SELECT N'value'")
          expect(result)
            .toBe("""
        SELECT
          N'value'
                    """.trimIndent())
        }

        it("formats SELECT query with OUTER APPLY") {
          val result = format("SELECT a, b FROM t OUTER APPLY fn(t.id)")
          expect(result)
            .toBe(
              """
        SELECT
          a,
          b
        FROM
          t
          OUTER APPLY fn(t.id)
                    """.trimIndent()
            )
        }

        it("formats Oracle recursive sub queries") {
          val result =
            format(
              """
        WITH t1(id, parent_id) AS (
          -- Anchor member.
          SELECT
            id,
            parent_id
          FROM
            tab1
          WHERE
            parent_id IS NULL
          MINUS
            -- Recursive member.
          SELECT
            t2.id,
            t2.parent_id
          FROM
            tab1 t2,
            t1
          WHERE
            t2.parent_id = t1.id
        ) SEARCH BREADTH FIRST BY id SET order1,
        another AS (SELECT * FROM dual)
        SELECT id, parent_id FROM t1 ORDER BY order1;
                    """.trimIndent()
            )
          expect(result)
            .toBe(
              """
        WITH t1(id, parent_id) AS (
          -- Anchor member.
          SELECT
            id,
            parent_id
          FROM
            tab1
          WHERE
            parent_id IS NULL
          MINUS
          -- Recursive member.
          SELECT
            t2.id,
            t2.parent_id
          FROM
            tab1 t2,
            t1
          WHERE
            t2.parent_id = t1.id
        ) SEARCH BREADTH FIRST BY id SET order1,
        another AS (
          SELECT
            *
          FROM
            dual
        )
        SELECT
          id,
          parent_id
        FROM
          t1
        ORDER BY
          order1;
                    """.trimIndent()
            )
        }

        it("formats Oracle recursive sub queries regardless of capitalization") {
          val result =
            format(
              """
        WITH t1(id, parent_id) AS (
          -- Anchor member.
          SELECT
            id,
            parent_id
          FROM
            tab1
          WHERE
            parent_id IS NULL
          MINUS
            -- Recursive member.
          SELECT
            t2.id,
            t2.parent_id
          FROM
            tab1 t2,
            t1
          WHERE
            t2.parent_id = t1.id
        ) SEARCH BREADTH FIRST by id set order1,
        another AS (SELECT * FROM dual)
        SELECT id, parent_id FROM t1 ORDER BY order1;
                    """.trimIndent()
            )
          expect(result)
            .toBe(
              """
        WITH t1(id, parent_id) AS (
          -- Anchor member.
          SELECT
            id,
            parent_id
          FROM
            tab1
          WHERE
            parent_id IS NULL
          MINUS
          -- Recursive member.
          SELECT
            t2.id,
            t2.parent_id
          FROM
            tab1 t2,
            t1
          WHERE
            t2.parent_id = t1.id
        ) SEARCH BREADTH FIRST by id set order1,
        another AS (
          SELECT
            *
          FROM
            dual
        )
        SELECT
          id,
          parent_id
        FROM
          t1
        ORDER BY
          order1;
                    """.trimIndent()
            )
        }
      }
    }
  })
