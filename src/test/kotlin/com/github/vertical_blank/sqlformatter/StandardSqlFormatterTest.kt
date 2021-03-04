package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.enums.StringLiteral
import com.github.vertical_blank.sqlformatter.features.supportsAlterTable
import com.github.vertical_blank.sqlformatter.features.supportsBetween
import com.github.vertical_blank.sqlformatter.features.supportsCase
import com.github.vertical_blank.sqlformatter.features.supportsCreateTable
import com.github.vertical_blank.sqlformatter.features.supportsJoin
import com.github.vertical_blank.sqlformatter.features.supportsSchema
import com.github.vertical_blank.sqlformatter.features.supportsStrings
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object StandardSqlFormatterTest :
    Spek({
      val formatter = SqlFormatter.standard()

      describe("StandardSqlFormatter") {
        with(formatter) {
          behavesLikeSqlFormatter(formatter)
          supportsCase(formatter)
          supportsCreateTable(formatter)
          supportsAlterTable(formatter)
          supportsStrings(formatter, listOf(StringLiteral.DoubleQuote, StringLiteral.SingleQuote))
          supportsBetween(formatter)
          supportsSchema(formatter)
          supportsJoin(formatter)

          it("replaces ? indexed placeholders with param values") {
            val result = format("SELECT ?, ?, ?;", listOf("first", "second", "third"))

            expect(result)
                .toBe(
                    """
            SELECT
              first,
              second,
              third;
                    """.trimIndent())
          }

          it("formats FETCH FIRST like LIMIT") {
            val result = format("SELECT * FETCH FIRST 2 ROWS ONLY;")
            expect(result)
                .toBe(
                    """
            SELECT
              *
            FETCH FIRST
              2 ROWS ONLY;
                    """.trimIndent())
          }

          it("Sql and comments in single line") {
            val result =
                format(
                    "SELECT * FROM table/* comment */ WHERE date BETWEEN '2001-01-01' AND '2010-12-31'; -- comment")
            expect(result)
                .toBe(
                    """
            SELECT
              *
            FROM
              table
              /* comment */
            WHERE
              date BETWEEN '2001-01-01' AND '2010-12-31';
            -- comment
                    """.trimIndent())
          }

          it("Formats merge") {
            val result =
                format(
                    """merge into DW_STG_USER.ACCOUNT_DIM target using ( select COMMON_NAME m_commonName, ORIGIN m_origin, USAGE_TYPE m_usageType, CATEGORY m_category from MY_TABLE where USAGE_TYPE = :value ) source on source.m_usageType = target.USAGE_TYPE when matched then update set target.COMMON_NAME = source.m_commonName, target.ORIGIN = source.m_origin, target.USAGE_TYPE = source.m_usageType, target.CATEGORY = source.m_category where ((source.m_commonName <> target.COMMON_NAME)or(source.m_origin <> target.ORIGIN)or(source.m_usageType <> target.USAGE_TYPE)or(source.m_category <> target.CATEGORY)) when not matched then insert ( target.COMMON_NAME, target.ORIGIN, target.USAGE_TYPE, target.CATEGORY) values (source.m_commonName, source.m_origin, source.m_usageType, source.m_category)""")
            expect(result)
                .toBe(
                    """
          merge into DW_STG_USER.ACCOUNT_DIM target using (
            select
              COMMON_NAME m_commonName,
              ORIGIN m_origin,
              USAGE_TYPE m_usageType,
              CATEGORY m_category
            from
              MY_TABLE
            where
              USAGE_TYPE =: value
          ) source on source.m_usageType = target.USAGE_TYPE
          when matched then
          update
          set
            target.COMMON_NAME = source.m_commonName,
            target.ORIGIN = source.m_origin,
            target.USAGE_TYPE = source.m_usageType,
            target.CATEGORY = source.m_category
          where
            (
              (source.m_commonName <> target.COMMON_NAME)
              or (source.m_origin <> target.ORIGIN)
              or (source.m_usageType <> target.USAGE_TYPE)
              or (source.m_category <> target.CATEGORY)
            )
            when not matched then insert (
              target.COMMON_NAME,
              target.ORIGIN,
              target.USAGE_TYPE,
              target.CATEGORY
            )
          values
            (
              source.m_commonName,
              source.m_origin,
              source.m_usageType,
              source.m_category
            )
                  """.trimIndent())
          }
        }
      }
    })
