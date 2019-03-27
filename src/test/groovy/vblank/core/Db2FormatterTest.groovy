package vblank.core

import groovy.transform.TypeChecked
import org.junit.Test
import vblank.SqlFormatter

@TypeChecked
class Db2FormatterTest extends FormatterTestBase {

    @Override
    String lang() {
        return 'db2'
    }

    @Test
    void "formats FETCH FIRST like LIMIT"() {
        expect(SqlFormatter.format(
                "SELECT col1 FROM tbl ORDER BY col2 DESC FETCH FIRST 20 ROWS ONLY;",
                langConfig()
        )).toBe(
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
        String result = SqlFormatter.format(
                "SELECT col FROM\n" +
                        "-- This is a comment\n" +
                        "MyTable;\n",
                langConfig()
        )
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
        String result = SqlFormatter.format(
                "SELECT col#1, @col2 FROM tbl\n",
                langConfig()
        )
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
        expect(SqlFormatter.format("SELECT :variable;", langConfig())).toBe(
                "SELECT\n" +
                        "  :variable;"
        )
    }

    @Test
    void "replaces :variables with param values"() {
        String result = SqlFormatter.format(
                "SELECT :variable",
                langConfig().toBuilder().params(Params.Holder.of(["variable": "\"variable value\""])).build()
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  \"variable value\""
        )
    }
}
