package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.core.FormatConfig
import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.*

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.amshove.kluent.*

import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsCase(formatter: SqlFormatter.Formatter) { with(formatter) {
  it("formats CASE ... WHEN with a blank expression") {
    val result = format(
      "CASE WHEN option = 'foo' THEN 1 WHEN option = 'bar' THEN 2 WHEN option = 'baz' THEN 3 ELSE 4 END;"
    );

    expect(result).toBe("""
      CASE
        WHEN option = 'foo' THEN 1
        WHEN option = 'bar' THEN 2
        WHEN option = 'baz' THEN 3
        ELSE 4
      END;
    """.trimIndent());
  }

  it("formats CASE ... WHEN with an expression") {
    val result = format(
      "CASE toString(getNumber()) WHEN 'one' THEN 1 WHEN 'two' THEN 2 WHEN 'three' THEN 3 ELSE 4 END;"
    );

    expect(result).toBe("""
      CASE
        toString(getNumber())
        WHEN 'one' THEN 1
        WHEN 'two' THEN 2
        WHEN 'three' THEN 3
        ELSE 4
      END;
    """.trimIndent());
  }

  it("formats CASE ... WHEN inside SELECT") {
    val result = format(
      "SELECT foo, bar, CASE baz WHEN 'one' THEN 1 WHEN 'two' THEN 2 ELSE 3 END FROM table"
    );

    expect(result).toBe("""
      SELECT
        foo,
        bar,
        CASE
          baz
          WHEN 'one' THEN 1
          WHEN 'two' THEN 2
          ELSE 3
        END
      FROM
        table
    """.trimIndent());
  }

  it("recognizes lowercase CASE ... END") {
    val result = format("case when option = 'foo' then 1 else 2 end;");

    expect(result).toBe("""
      case
        when option = 'foo' then 1
        else 2
      end;
    """.trimIndent());
  }

  // Regression test for issue #43
  it("ignores words CASE and END inside other strings") {
    val result = format("SELECT CASEDATE, ENDDATE FROM table1;");

    expect(result).toBe("""
      SELECT
        CASEDATE,
        ENDDATE
      FROM
        table1;
    """.trimIndent());
  }

  it("properly converts to uppercase in case statements") {
    val result = format(
      "case toString(getNumber()) when 'one' then 1 when 'two' then 2 when 'three' then 3 else 4 end;",
      FormatConfig.builder().uppercase(true).build()
    );
    expect(result).toBe("""
      CASE
        toString(getNumber())
        WHEN 'one' THEN 1
        WHEN 'two' THEN 2
        WHEN 'three' THEN 3
        ELSE 4
      END;
    """.trimIndent());
  }

}}
