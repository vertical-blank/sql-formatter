package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.expect
import com.github.vertical_blank.sqlformatter.languages.StringLiteral
import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsStrings(formatter: SqlFormatter.Formatter, stringTypes: List<String> = listOf()) {
  with(formatter) {
    if (stringTypes.contains(StringLiteral.DOUBLE_QUOTE)) {
      it("supports double-quoted strings") {
        expect(format(""""foo JOIN bar"""")).toBe(""""foo JOIN bar"""")
        expect(format(""""foo \" JOIN bar"""")).toBe(""""foo \" JOIN bar"""")
      }
    }

    if (stringTypes.contains(StringLiteral.SINGLE_QUOTE)) {
      it("supports single-quoted strings") {
        expect(format("'foo JOIN bar'")).toBe("'foo JOIN bar'")
        expect(format("'foo \\' JOIN bar'")).toBe("'foo \\' JOIN bar'")
      }
    }

    if (stringTypes.contains(StringLiteral.BACK_QUOTE)) {
      it("supports backtick-quoted strings") {
        expect(format("`foo JOIN bar`")).toBe("`foo JOIN bar`")
        expect(format("`foo `` JOIN bar`")).toBe("`foo `` JOIN bar`")
      }
    }

    if (stringTypes.contains(StringLiteral.U_DOUBLE_QUOTE)) {
      it("supports unicode double-quoted strings") {
        expect(format("""U&"foo JOIN bar"""")).toBe("""U&"foo JOIN bar"""")
        expect(format("""U&"foo \" JOIN bar"""")).toBe("""U&"foo \" JOIN bar"""")
      }
    }

    if (stringTypes.contains(StringLiteral.U_SINGLE_QUOTE)) {
      it("supports single-quoted strings") {
        expect(format("U&'foo JOIN bar'")).toBe("U&'foo JOIN bar'")
        expect(format("U&'foo \\' JOIN bar'")).toBe("U&'foo \\' JOIN bar'")
      }
    }

    if (stringTypes.contains(StringLiteral.DOLLAR)) {
      it("supports DOLLAR-quoted strings") {
        expect(format("\$xxx\$foo \$\$ LEFT JOIN \$yyy\$ bar\$xxx\$"))
          .toBe("\$xxx\$foo \$\$ LEFT JOIN \$yyy\$ bar\$xxx\$")
        expect(format("\$\$foo JOIN bar\$\$")).toBe("\$\$foo JOIN bar\$\$")
        expect(format("\$\$foo \$ JOIN bar\$\$")).toBe("\$\$foo $ JOIN bar\$\$")
        expect(format("\$\$foo \n bar\$\$")).toBe("\$\$foo \n bar\$\$")
      }
    }

    if (stringTypes.contains(StringLiteral.BRACKET)) {
      it("supports [BRACKET-quoted identifiers]") {
        expect(format("[foo JOIN bar]")).toBe("[foo JOIN bar]")
        expect(format("[foo ]] JOIN bar]")).toBe("[foo ]] JOIN bar]")
      }
    }

    if (stringTypes.contains(StringLiteral.N_SINGLE_QUOTE)) {
      it("supports T-SQL unicode strings") {
        expect(format("N'foo JOIN bar'")).toBe("N'foo JOIN bar'")
        expect(format("N'foo \\' JOIN bar'")).toBe("N'foo \\' JOIN bar'")
      }
    }

    if (stringTypes.contains(StringLiteral.Q_SINGLE_QUOTE)) {
      it("supports Oracle quotation operator") {
        expect(format("Q'[I'm boy]',Q'{I'm boy}',Q'<I'm boy>',Q'(I'm boy)',1"))
          .toBe(
            """Q'[I'm boy]',
            |Q'{I'm boy}',
            |Q'<I'm boy>',
            |Q'(I'm boy)',
            |1""".trimMargin()
          )
        expect(format("NQ'[I'm boy]',NQ'{I'm boy}',NQ'<I'm boy>',NQ'(I'm boy)',1"))
          .toBe(
            """NQ'[I'm boy]',
            |NQ'{I'm boy}',
            |NQ'<I'm boy>',
            |NQ'(I'm boy)',
            |1""".trimMargin()
          )
      }
    }
  }
}
