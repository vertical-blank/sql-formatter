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

object MariaDbFormatterTest: Spek({
  val formatter = SqlFormatter.of(Dialect.MariaDb)

  describe("MariaDbFormatter") { with(formatter) {
    behavesLikeMariaDbFormatter(formatter);
  }}

})
