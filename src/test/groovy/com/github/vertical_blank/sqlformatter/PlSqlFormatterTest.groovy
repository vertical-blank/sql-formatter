package com.github.vertical_blank.sqlformatter

import groovy.transform.TypeChecked
import org.junit.jupiter.api.Test

@TypeChecked
class PlSqlFormatterTest extends FormatterTestBase {

    @Override
    String lang() {
        return 'pl/sql'
    }

    @Test
    void "formats FETCH FIRST like LIMIT"() {
        expect(formatWithLang(
                "SELECT col1 FROM tbl ORDER BY col2 DESC FETCH FIRST 20 ROWS ONLY;"
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
    }

    @Test
    void "formats only -- as a line comment"() {
        String result = formatWithLang(
                "SELECT col FROM\n" +
                        "-- This is a comment\n" +
                        "MyTable;\n"
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
    //recognizes _, \$, #, . and @ as part of identifiers
    void "recognizes  as part of identifiers"() {
        String result = formatWithLang(
                "SELECT my_col\$1#, col.2@ FROM tbl\n"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  my_col\$1#,\n" +
                        "  col.2@\n" +
                        "FROM\n" +
                        "  tbl"
        )
    }

    @Test
    void "formats short CREATE TABLE"() {
        expect(formatWithLang(
                "CREATE TABLE items (a INT PRIMARY KEY, b TEXT)"
        )).toBe(
                "CREATE TABLE items (a INT PRIMARY KEY, b TEXT)"
        )
    }

    @Test
    void "formats long CREATE TABLE"() {
        expect(formatWithLang(
                "CREATE TABLE items (a INT PRIMARY KEY, b TEXT, c INT NOT NULL, d INT NOT NULL)"
        )).toBe(
                "CREATE TABLE items (\n" +
                        "  a INT PRIMARY KEY,\n" +
                        "  b TEXT,\n" +
                        "  c INT NOT NULL,\n" +
                        "  d INT NOT NULL\n" +
                        ")"
        )
    }

    @Test
    void "formats INSERT without INTO"() {
        String result = formatWithLang(
                "INSERT Customers (ID, MoneyBalance, Address, City) VALUES (12,-123.4, 'Skagen 2111','Stv')"
        )
        expect(result).toBe(
                "INSERT\n" +
                        "  Customers (ID, MoneyBalance, Address, City)\n" +
                        "VALUES\n" +
                        "  (12, -123.4, 'Skagen 2111', 'Stv')"
        )
    }

    @Test
    void "formats ALTER TABLE MODIFY query"() {
        String result = formatWithLang(
                "ALTER TABLE supplier MODIFY supplier_name char(100) NOT NULL;"
        )
        expect(result).toBe(
                "ALTER TABLE\n" +
                        "  supplier\n" +
                        "MODIFY\n" +
                        "  supplier_name char(100) NOT NULL;"
        )
    }

    @Test
    void "formats ALTER TABLE ALTER COLUMN query"() {
        String result = formatWithLang(
                "ALTER TABLE supplier ALTER COLUMN supplier_name VARCHAR(100) NOT NULL;"
        )
        expect(result).toBe(
                "ALTER TABLE\n" +
                        "  supplier\n" +
                        "ALTER COLUMN\n" +
                        "  supplier_name VARCHAR(100) NOT NULL;"
        )
    }

//    Originally, this Test is run against StandardSql.
//    @Test
//    void "recognizes [] strings"() {
//        expect(formatWithLang("[foo JOIN bar]")).toBe("[foo JOIN bar]")
//        expect(formatWithLang("[foo ]] JOIN bar]")).toBe("[foo ]] JOIN bar]")
//    }
//
//    Originally, this Test is run against StandardSql.
//    @Test
//    void "recognizes :variables"() {
//        String result = formatWithLang(
//                "SELECT :variable, :a1_2.3\$, :'var name', :\"var name\", :`var name`, :[var name];"
//        )
//        expect(result).toBe(
//                "SELECT\n" +
//                        "  :variable,\n" +
//                        "  :a1_2.3\$,\n" +
//                        "  :'var name',\n" +
//                        "  :\"var name\",\n" +
//                        "  :`var name`,\n" +
//                        "  :[var name];"
//        )
//    }
//
//    Originally, this Test is run against StandardSql.
//    @Test
//    void "replaces :variables with param values"() {
//        String result = formatWithLang(
//                "SELECT :variable, :a1_2.3\$, :'var name', :\"var name\", :`var name`," +
//                        " :[var name], :'escaped \\'var\\'', :\"^*& weird \\\" var   \";",
//                [
//                        "variable"           : "\"variable value\"",
//                        "a1_2.3\$"           : "'weird value'",
//                        "var name"           : "'var value'",
//                        "escaped 'var'"      : "'weirder value'",
//                        "^*& weird \" var   ": "'super weird value'"
//                ]
//        )
//        expect(result).toBe(
//                "SELECT\n" +
//                        "  \"variable value\",\n" +
//                        "  'weird value',\n" +
//                        "  'var value',\n" +
//                        "  'var value',\n" +
//                        "  'var value',\n" +
//                        "  'var value',\n" +
//                        "  'weirder value',\n" +
//                        "  'super weird value';"
//        )
//    }

//    @Test
//    void "recognizes ?[0-9]* placeholders"() {
//        String result = formatWithLang("SELECT ?1, ?25, ?;")
//        expect(result).toBe(
//                "SELECT\n" +
//                        "  ?1,\n" +
//                        "  ?25,\n" +
//                        "  ?;"
//        )
//    }

    @Test
    void "replaces ? numbered placeholders with param values"() {
        String result = formatWithLang("SELECT ?1, ?2, ?0;",
                [
                        '0': "first",
                        '1': "second",
                        '2': "third"
                ])
        expect(result).toBe(
                "SELECT\n" +
                        "  second,\n" +
                        "  third,\n" +
                        "  first;"
        )
    }

    @Test
    void "replaces ? indexed placeholders with param values"() {
        String result = formatWithLang("SELECT ?, ?, ?;", ["first", "second", "third"])
        expect(result).toBe(
                "SELECT\n" +
                        "  first,\n" +
                        "  second,\n" +
                        "  third;"
        )
    }

    @Test
    void "formats SELECT query with CROSS JOIN"() {
        String result = formatWithLang("SELECT a, b FROM t CROSS JOIN t2 on t.id = t2.id_t")
        expect(result).toBe(
                "SELECT\n" +
                        "  a,\n" +
                        "  b\n" +
                        "FROM\n" +
                        "  t\n" +
                        "  CROSS JOIN t2 on t.id = t2.id_t"
        )
    }

    @Test
    void "formats SELECT query with CROSS APPLY"() {
        String result = formatWithLang("SELECT a, b FROM t CROSS APPLY fn(t.id)")
        expect(result).toBe(
                "SELECT\n" +
                        "  a,\n" +
                        "  b\n" +
                        "FROM\n" +
                        "  t\n" +
                        "  CROSS APPLY fn(t.id)"
        )
    }

    @Test
    void "formats simple SELECT"() {
        String result = formatWithLang("SELECT N, M FROM t")
        expect(result).toBe(
                "SELECT\n" +
                        "  N,\n" +
                        "  M\n" +
                        "FROM\n" +
                        "  t"
        )
    }

    @Test
    void "formats simple SELECT with national characters"() {
        String result = formatWithLang("SELECT N'value'")
        expect(result).toBe(
                "SELECT\n" +
                        "  N'value'"
        )
    }

    @Test
    void "formats SELECT query with OUTER APPLY"() {
        String result = formatWithLang("SELECT a, b FROM t OUTER APPLY fn(t.id)")
        expect(result).toBe(
                "SELECT\n" +
                        "  a,\n" +
                        "  b\n" +
                        "FROM\n" +
                        "  t\n" +
                        "  OUTER APPLY fn(t.id)"
        )
    }

    @Test
    void "formats CASE WHEN with a blank expression"() {
        String result = formatWithLang(
                "CASE WHEN option = 'foo' THEN 1 WHEN option = 'bar' THEN 2 WHEN option = 'baz' THEN 3 ELSE 4 END;"
        )

        expect(result).toBe(
                "CASE\n" +
                        "  WHEN option = 'foo' THEN 1\n" +
                        "  WHEN option = 'bar' THEN 2\n" +
                        "  WHEN option = 'baz' THEN 3\n" +
                        "  ELSE 4\n" +
                        "END;"
        )
    }

    @Test
    void "formats CASE WHEN inside SELECT"() {
        String result = formatWithLang(
                "SELECT foo, bar, CASE baz WHEN 'one' THEN 1 WHEN 'two' THEN 2 ELSE 3 END FROM table"
        )

        expect(result).toBe(
                "SELECT\n" +
                        "  foo,\n" +
                        "  bar,\n" +
                        "  CASE\n" +
                        "    baz\n" +
                        "    WHEN 'one' THEN 1\n" +
                        "    WHEN 'two' THEN 2\n" +
                        "    ELSE 3\n" +
                        "  END\n" +
                        "FROM\n" +
                        "  table"
        )
    }

    @Test
    void "formats CASE WHEN with an expression"() {
        String result = formatWithLang(
                "CASE toString(getNumber()) WHEN 'one' THEN 1 WHEN 'two' THEN 2 WHEN 'three' THEN 3 ELSE 4 END;"
        )

        expect(result).toBe(
                "CASE\n" +
                        "  toString(getNumber())\n" +
                        "  WHEN 'one' THEN 1\n" +
                        "  WHEN 'two' THEN 2\n" +
                        "  WHEN 'three' THEN 3\n" +
                        "  ELSE 4\n" +
                        "END;"
        )
    }

}
