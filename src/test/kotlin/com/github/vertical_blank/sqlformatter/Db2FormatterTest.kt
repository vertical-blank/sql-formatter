package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.enums.StringLiteral
import com.github.vertical_blank.sqlformatter.features.supportsAlterTable
import com.github.vertical_blank.sqlformatter.features.supportsBetween
import com.github.vertical_blank.sqlformatter.features.supportsCreateTable
import com.github.vertical_blank.sqlformatter.features.supportsSchema
import com.github.vertical_blank.sqlformatter.features.supportsStrings
import com.github.vertical_blank.sqlformatter.languages.Dialect
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object Db2FormatterTest :
    Spek({
      val formatter = SqlFormatter.of(Dialect.Db2)

      describe("Db2Formatter") {
        with(formatter) {
          behavesLikeSqlFormatter(formatter)
          supportsCreateTable(formatter)
          supportsAlterTable(formatter)
          supportsStrings(
              formatter,
              listOf(StringLiteral.DoubleQuote, StringLiteral.SingleQuote, StringLiteral.BackQuote))
          supportsBetween(formatter)
          supportsSchema(formatter)

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
                        """.trimIndent())
          }
        }
      }
    })
