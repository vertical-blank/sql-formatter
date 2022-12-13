package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.features.supportsAlterTable
import com.github.vertical_blank.sqlformatter.features.supportsBetween
import com.github.vertical_blank.sqlformatter.features.supportsCase
import com.github.vertical_blank.sqlformatter.features.supportsCreateTable
import com.github.vertical_blank.sqlformatter.features.supportsJoin
import com.github.vertical_blank.sqlformatter.features.supportsOperators
import com.github.vertical_blank.sqlformatter.features.supportsStrings
import com.github.vertical_blank.sqlformatter.languages.StringLiteral
import org.spekframework.spek2.style.specification.Suite

fun Suite.behavesLikeMariaDbFormatter(formatter: SqlFormatter.Formatter) {
  with(formatter) {
    behavesLikeSqlFormatter(formatter)
    supportsCase(formatter)
    supportsCreateTable(formatter)
    supportsAlterTable(formatter)
    supportsStrings(
      formatter,
      listOf(StringLiteral.DOUBLE_QUOTE, StringLiteral.SINGLE_QUOTE, StringLiteral.BACK_QUOTE)
    )
    supportsBetween(formatter)
    supportsOperators(
      formatter,
      listOf(
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
      )
    )
    supportsJoin(
      formatter,
      without = listOf("FULL"),
      additionally =
        listOf(
          "STRAIGHT_JOIN",
          "NATURAL LEFT JOIN",
          "NATURAL LEFT OUTER JOIN",
          "NATURAL RIGHT JOIN",
          "NATURAL RIGHT OUTER JOIN",
        )
    )

    it("supports # comments") {
      expect(format("SELECT a # comment\nFROM b # comment"))
        .toBe(
          """
      SELECT
        a # comment
      FROM
        b # comment
                """.trimIndent()
        )
    }

    it("supports @variables") {
      expect(format("SELECT @foo, @bar"))
        .toBe("""
      SELECT
        @foo,
        @bar
                """.trimIndent())
    }

    it("supports setting variables: @var :=") {
      expect(format("SET @foo := (SELECT * FROM tbl);"))
        .toBe(
          """
      SET
        @foo := (
          SELECT
            *
          FROM
            tbl
        );
                """.trimIndent()
        )
    }
  }
}
