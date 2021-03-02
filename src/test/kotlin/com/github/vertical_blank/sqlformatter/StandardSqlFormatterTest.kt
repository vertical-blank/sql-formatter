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
        }
      }
    })
