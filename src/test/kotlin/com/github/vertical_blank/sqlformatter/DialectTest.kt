package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.languages.Dialect
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object DialectTest :
    Spek({
      describe("Dialect") {
        it("Find dialect by name or alias") {
          expect(Dialect.nameOf("pl/sql").get()).toBe(Dialect.PlSql)
          expect(Dialect.nameOf("plsql").get()).toBe(Dialect.PlSql)
        }
      }
    })
