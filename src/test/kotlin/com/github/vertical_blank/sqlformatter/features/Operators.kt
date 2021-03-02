package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.expect
import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsOperators(formatter: SqlFormatter.Formatter, operators: List<String>) {
  with(formatter) {
    operators.forEach { op ->
      it("supports $op operator") { expect(format("foo${op}bar")).toBe("foo $op bar") }
    }
  }
}
