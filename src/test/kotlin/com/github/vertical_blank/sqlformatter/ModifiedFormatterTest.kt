package com.github.vertical_blank.sqlformatter

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ModifiedFormatterTest :
    Spek({
      val formatter = SqlFormatter.standard().modify { it.plusOperators("=>") }

      describe("ModifiedFormatter") {
        with(formatter) {
          it("formats fat arrow operator") {
            val result = format("SELECT * FROM TABLE WHERE A => 4")
            expect(result)
                .toBe(
                    """
        SELECT
          *
        FROM
          TABLE
        WHERE
          A => 4""".trimIndent())
          }
        }
      }
    })
