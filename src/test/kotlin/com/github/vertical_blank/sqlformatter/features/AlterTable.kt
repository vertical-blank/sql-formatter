package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.expect
import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsAlterTable(formatter: SqlFormatter.Formatter) {
  with(formatter) {
    it("formats ALTER TABLE ... ALTER COLUMN query") {
      val result = format("ALTER TABLE supplier ALTER COLUMN supplier_name VARCHAR(100) NOT NULL;")
      expect(result)
        .toBe(
          """
      ALTER TABLE
        supplier
      ALTER COLUMN
        supplier_name VARCHAR(100) NOT NULL;
                """.trimIndent()
        )
    }
  }
}
