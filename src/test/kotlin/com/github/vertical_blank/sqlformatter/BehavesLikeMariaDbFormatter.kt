package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.core.FormatConfig
import com.github.vertical_blank.sqlformatter.features.*
import com.github.vertical_blank.sqlformatter.enums.StringLiteral

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.amshove.kluent.*

import org.spekframework.spek2.style.specification.Suite

fun Suite.behavesLikeMariaDbFormatter(formatter: SqlFormatter.Formatter) { with(formatter) {
  behavesLikeSqlFormatter(formatter);
  supportsCase(formatter);
  supportsCreateTable(formatter);
  supportsAlterTable(formatter);
  supportsStrings(formatter, listOf(StringLiteral.DoubleQuote, StringLiteral.SingleQuote, StringLiteral.BackQuote));
  supportsBetween(formatter);
  supportsOperators(formatter, listOf(
    "%",
    "&",
    "|",
    "^",
    "~",
    "!=",
    "!",
    "<=>",
    "<<",
    ">>",
    "&&",
    "||",
    ":=",
  ));
  supportsJoin(formatter,
    without = listOf("FULL"),
    additionally = listOf(
      "STRAIGHT_JOIN",
      "NATURAL LEFT JOIN",
      "NATURAL LEFT OUTER JOIN",
      "NATURAL RIGHT JOIN",
      "NATURAL RIGHT OUTER JOIN",
    ))

  it("supports # comments") {
    expect(format("SELECT a # comment\nFROM b # comment")).toBe("""
      SELECT
        a # comment
      FROM
        b # comment
    """.trimIndent());
  }

  it("supports @variables") {
    expect(format("SELECT @foo, @bar")).toBe("""
      SELECT
        @foo,
        @bar
    """.trimIndent());
  }

  it("supports setting variables: @var :=") {
    expect(format("SET @foo := (SELECT * FROM tbl);")).toBe("""
      SET
        @foo := (
          SELECT
            *
          FROM
            tbl
        );
    """.trimIndent());
  }

}}
