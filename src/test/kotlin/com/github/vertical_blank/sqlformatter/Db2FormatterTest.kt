package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.core.FormatConfig

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.amshove.kluent.*
import com.github.vertical_blank.sqlformatter.features.*
import com.github.vertical_blank.sqlformatter.languages.Dialect

import com.github.vertical_blank.sqlformatter.enums.StringLiteral

object Db2FormatterTest: Spek({
  val formatter = SqlFormatter.of(Dialect.Db2)

  describe("Db2Formatter") { with(formatter) {
    behavesLikeSqlFormatter(formatter)
    supportsCreateTable(formatter)
    supportsAlterTable(formatter)
    supportsStrings(formatter, listOf(StringLiteral.DoubleQuote, StringLiteral.SingleQuote, StringLiteral.BackQuote));
    supportsBetween(formatter)
    supportsSchema(formatter)

    it("formats FETCH FIRST like LIMIT") {
      expect(format("SELECT col1 FROM tbl ORDER BY col2 DESC FETCH FIRST 20 ROWS ONLY;"))
      .toBe("""
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
  }}

})
