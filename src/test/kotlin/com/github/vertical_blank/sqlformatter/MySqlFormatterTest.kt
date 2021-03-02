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

object MySqlFormatterTest: Spek({
  val formatter = SqlFormatter.of(Dialect.MySql)

  describe("MySqlFormatter") { with(formatter) {
    behavesLikeMariaDbFormatter(formatter);

    describe("additional MySQL operators") {
      supportsOperators(formatter, listOf("->", "->>"));
    }
  }}
})
