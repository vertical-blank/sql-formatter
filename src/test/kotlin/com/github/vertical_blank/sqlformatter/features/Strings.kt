package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.core.FormatConfig
import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.*
import com.github.vertical_blank.sqlformatter.enums.StringLiteral;

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.collections.emptyList
import org.amshove.kluent.*

import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsStrings(formatter: SqlFormatter.Formatter, stringTypes: List<StringLiteral> = listOf()) { with(formatter) {
  if (stringTypes.contains(StringLiteral.DoubleQuote)) {
    it("supports double-quoted strings") {
      expect(format(""""foo JOIN bar"""")).toBe(""""foo JOIN bar"""");
      expect(format(""""foo \" JOIN bar"""")).toBe(""""foo \" JOIN bar"""");
    }
  }

  if (stringTypes.contains(StringLiteral.SingleQuote)) {
    it("supports single-quoted strings") {
      expect(format("'foo JOIN bar'")).toBe("'foo JOIN bar'");
      expect(format("'foo \\' JOIN bar'")).toBe("'foo \\' JOIN bar'");
    }
  }

  if (stringTypes.contains(StringLiteral.BackQuote)) {
    it("supports backtick-quoted strings") {
      expect(format("`foo JOIN bar`")).toBe("`foo JOIN bar`");
      expect(format("`foo `` JOIN bar`")).toBe("`foo `` JOIN bar`");
    }
  }

  if (stringTypes.contains(StringLiteral.UDoubleQuote)) {
    it("supports unicode double-quoted strings") {
      expect(format("""U&"foo JOIN bar"""")).toBe("""U&"foo JOIN bar"""");
      expect(format("""U&"foo \" JOIN bar"""")).toBe("""U&"foo \" JOIN bar"""");
    }
  }

  if (stringTypes.contains(StringLiteral.USingleQuote)) {
    it("supports single-quoted strings") {
      expect(format("U&'foo JOIN bar'")).toBe("U&'foo JOIN bar'");
      expect(format("U&'foo \\' JOIN bar'")).toBe("U&'foo \\' JOIN bar'");
    }
  }

  if (stringTypes.contains(StringLiteral.Dollar)) {
    it("supports dollar-quoted strings") {
      expect(format("\$xxx\$foo \$\$ LEFT JOIN \$yyy\$ bar\$xxx\$")).toBe(
        "\$xxx\$foo \$\$ LEFT JOIN \$yyy\$ bar\$xxx\$"
      );
      expect(format("\$\$foo JOIN bar\$\$")).toBe("\$\$foo JOIN bar\$\$");
      expect(format("\$\$foo \$ JOIN bar\$\$")).toBe("\$\$foo $ JOIN bar\$\$");
      expect(format("\$\$foo \n bar\$\$")).toBe("\$\$foo \n bar\$\$");
    }
  }

  if (stringTypes.contains(StringLiteral.Bracket)) {
    it("supports [bracket-quoted identifiers]") {
      expect(format("[foo JOIN bar]")).toBe("[foo JOIN bar]");
      expect(format("[foo ]] JOIN bar]")).toBe("[foo ]] JOIN bar]");
    }
  }

  if (stringTypes.contains(StringLiteral.NSingleQuote)) {
    it("supports T-SQL unicode strings") {
      expect(format("N'foo JOIN bar'")).toBe("N'foo JOIN bar'");
      expect(format("N'foo \\' JOIN bar'")).toBe("N'foo \\' JOIN bar'");
    }
  }
}}
