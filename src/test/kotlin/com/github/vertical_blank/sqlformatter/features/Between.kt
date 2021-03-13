package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.expect
import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsBetween(formatter: SqlFormatter.Formatter) {
  with(formatter) {
    it("formats BETWEEN _ AND _ on single line") {
      expect(format("foo BETWEEN bar AND baz")).toBe("foo BETWEEN bar AND baz")
    }
  }
}
