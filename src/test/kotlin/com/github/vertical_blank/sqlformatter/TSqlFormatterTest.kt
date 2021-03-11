package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.features.supportsAlterTable
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

object TSqlFormatterTest :
    Spek({
      val formatter = SqlFormatter.of(Dialect.TSql)

      describe("TSqlFormatter") {
        with(formatter) {
          behavesLikeSqlFormatter(formatter)
          supportsCase(formatter)
          supportsCreateTable(formatter)
          supportsAlterTable(formatter)
          supportsStrings(
              formatter,
              listOf(
                  StringLiteral.DOUBLE_QUOTE,
                  StringLiteral.SINGLE_QUOTE,
                  StringLiteral.N_SINGLE_QUOTE,
                  StringLiteral.BRACKET))
          supportsBetween(formatter)
          supportsSchema(formatter)
          supportsOperators(
              formatter,
              listOf(
                  "%",
                  "&",
                  "|",
                  "^",
                  "~",
                  "!=",
                  "!<",
                  "!>",
                  "+=",
                  "-=",
                  "*=",
                  "/=",
                  "%=",
                  "|=",
                  "&=",
                  "^=",
                  "::",
              ))
          supportsJoin(formatter, without = listOf("NATURAL"))

          // TODO: The following are duplicated from StandardSQLFormatter test

          it("formats INSERT without INTO") {
            val result =
                format(
                    "INSERT Customers (ID, MoneyBalance, Address, City) VALUES (12,-123.4, 'Skagen 2111','Stv');")
            expect(result)
                .toBe(
                    """
        INSERT
          Customers (ID, MoneyBalance, Address, City)
        VALUES
          (12, -123.4, 'Skagen 2111', 'Stv');
                    """.trimIndent())
          }

          it("recognizes @variables") {
            val result = format("""SELECT @variable, @"var name", @[var name];""")
            expect(result)
                .toBe(
                    """
        SELECT
          @variable,
          @"var name",
          @[var name];
                    """.trimIndent())
          }

          it("replaces @variables with param values") {
            val result =
                format(
                    """SELECT @variable, @"var name1", @[var name2];""",
                    mapOf(
                        "variable" to "'var value'",
                        "var name1" to "'var value1'",
                        "var name2" to "'var value2'",
                    ))
            expect(result)
                .toBe(
                    """
        SELECT
          'var value',
          'var value1',
          'var value2';
                    """.trimIndent())
          }

          it("formats SELECT query with CROSS JOIN") {
            val result = format("SELECT a, b FROM t CROSS JOIN t2 on t.id = t2.id_t")
            expect(result)
                .toBe(
                    """
        SELECT
          a,
          b
        FROM
          t
          CROSS JOIN t2 on t.id = t2.id_t
                    """.trimIndent())
          }
        }
      }
    })
