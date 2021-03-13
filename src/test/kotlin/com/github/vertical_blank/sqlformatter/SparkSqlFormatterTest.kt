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

object SparkSqlFormatterTest :
    Spek({
      val formatter = SqlFormatter.of(Dialect.SparkSql)

      describe("SparkSqlFormatter") {
        with(formatter) {
          behavesLikeSqlFormatter(formatter)
          supportsCase(formatter)
          supportsCreateTable(formatter)
          supportsAlterTable(formatter)
          supportsStrings(
              formatter,
              listOf(
                  StringLiteral.DOUBLE_QUOTE, StringLiteral.SINGLE_QUOTE, StringLiteral.BACK_QUOTE))
          supportsBetween(formatter)
          supportsSchema(formatter)
          supportsOperators(
              formatter, listOf("!=", "%", "|", "&", "^", "~", "!", "<=>", "%", "&&", "||", "=="))
          supportsJoin(
              formatter,
              additionally =
                  listOf(
                      "ANTI JOIN",
                      "SEMI JOIN",
                      "LEFT ANTI JOIN",
                      "LEFT SEMI JOIN",
                      "RIGHT OUTER JOIN",
                      "RIGHT SEMI JOIN",
                      "NATURAL ANTI JOIN",
                      "NATURAL FULL OUTER JOIN",
                      "NATURAL INNER JOIN",
                      "NATURAL LEFT ANTI JOIN",
                      "NATURAL LEFT OUTER JOIN",
                      "NATURAL LEFT SEMI JOIN",
                      "NATURAL OUTER JOIN",
                      "NATURAL RIGHT OUTER JOIN",
                      "NATURAL RIGHT SEMI JOIN",
                      "NATURAL SEMI JOIN",
                  ))

          it("formats WINDOW specification as top level") {
            val result =
                format(
                    "SELECT *, LAG(value) OVER wnd AS next_value FROM tbl WINDOW wnd as (PARTITION BY id ORDER BY time);")
            expect(result)
                .toBe(
                    """
        SELECT
          *,
          LAG(value) OVER wnd AS next_value
        FROM
          tbl
        WINDOW
          wnd as (
            PARTITION BY
              id
            ORDER BY
              time
          );
                    """.trimIndent())
          }

          it("formats window function and end as inline") {
            val result =
                format(
                    """SELECT window(time, "1 hour").start AS window_start, window(time, "1 hour").end AS window_end FROM tbl;""")
            expect(result)
                .toBe(
                    """
        SELECT
          window(time, "1 hour").start AS window_start,
          window(time, "1 hour").end AS window_end
        FROM
          tbl;
                    """.trimIndent())
          }

          // eslint-disable-next-line no-template-curly-in-string
          it("does not add spaces around \${value} params") {
            // eslint-disable-next-line no-template-curly-in-string
            val result = format("SELECT \${var_name};")
            expect(result)
                .toBe(
                    """
        SELECT
          ${"$"}{var_name};
                    """.trimIndent())
          }

          // eslint-disable-next-line no-template-curly-in-string
          it("replaces \$variables and \${variables} with param values") {
            // eslint-disable-next-line no-template-curly-in-string
            val result =
                format(
                    "SELECT \$var1, \${var2};",
                    mapOf(
                        "var1" to "'var one'",
                        "var2" to "'var two'",
                    ),
                )
            expect(result)
                .toBe(
                    """
        SELECT
          'var one',
          'var two';
                    """.trimIndent())
          }
        }
      }
    })
