package com.github.vertical.blank

import groovy.transform.TypeChecked
import org.junit.jupiter.api.Test

@TypeChecked
class Db2FormatterTest extends FormatterTestBase {

    @Override
    String lang() {
        return 'db2'
    }

    @Test
    void "formats FETCH FIRST like LIMIT"() {
        expect(formatWithLang(
                "SELECT col1 FROM tbl ORDER BY col2 DESC FETCH FIRST 20 ROWS ONLY;")
        ).toBe(
                "SELECT\n" +
                        "  col1\n" +
                        "FROM\n" +
                        "  tbl\n" +
                        "ORDER BY\n" +
                        "  col2 DESC\n" +
                        "FETCH FIRST\n" +
                        "  20 ROWS ONLY;"
        )
    };

    @Test
    void "formats only -- as a line comment"() {
        String result = formatWithLang(
                "SELECT col FROM\n" +
                        "-- This is a comment\n" +
                        "MyTable;\n")
        expect(result).toBe(
                "SELECT\n" +
                        "  col\n" +
                        "FROM\n" +
                        "  -- This is a comment\n" +
                        "  MyTable;"
        )
    }

    @Test
    void "recognizes @ and # as part of identifiers"() {
        String result = formatWithLang(
                "SELECT col#1, @col2 FROM tbl\n")
        expect(result).toBe(
                "SELECT\n" +
                        "  col#1,\n" +
                        "  @col2\n" +
                        "FROM\n" +
                        "  tbl"
        )
    }

    @Test
    void "recognizes :variables"() {
        expect(formatWithLang("SELECT :variable;")).toBe(
                "SELECT\n" +
                        "  :variable;"
        )
    }

    @Test
    void "replaces :variables with param values"() {
        String result = formatWithLang(
                "SELECT :variable", ["variable": "\"variable value\""])
        expect(result).toBe(
                "SELECT\n" +
                        "  \"variable value\""
        )
    }
}
