package com.github.vertical_blank.sqlformatter

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ModifiedFormatterTest :
    Spek({
      describe("ModifiedFormatter") {
        it("With fat arrow operator") {
          {
            val result =
                SqlFormatter.standard()
                    .extend { it.plusOperators("=>") }
                    .format("SELECT * FROM TABLE WHERE A => 4")
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

        it("With := operator") {
          val result =
              SqlFormatter.standard()
                  .extend { it.plusOperators(":=") }
                  .format("SELECT * FROM TABLE WHERE A := 4")
          expect(result)
              .toBe(
                  """
        SELECT
          *
        FROM
          TABLE
        WHERE
          A := 4""".trimIndent())
        }
      }
    })
