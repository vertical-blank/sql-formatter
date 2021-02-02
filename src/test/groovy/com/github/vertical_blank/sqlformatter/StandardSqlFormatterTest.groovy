package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.core.FormatConfig
import groovy.transform.TypeChecked
import org.junit.jupiter.api.Test

@TypeChecked
class StandardSqlFormatterTest extends FormatterTestBase {

    @Override
    String lang() {
        return 'sql'
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

    @Test
    void "recognizes brackets strings"() {
        expect(formatWithLang("[foo JOIN bar]")).toBe("[foo JOIN bar]")
        expect(formatWithLang("[foo ]] JOIN bar]")).toBe("[foo ]] JOIN bar]")
    }

    @Test
    void "recognizes @variables"() {
        String result = formatWithLang(
                'SELECT @variable, @a1_2.3$, @\'var name\', @"var name", @`var name`, @[var name];'
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  @variable,\n" +
                        "  @a1_2.3\$,\n" +
                        "  @'var name',\n" +
                        "  @\"var name\",\n" +
                        "  @`var name`,\n" +
                        "  @[var name];"
        )
    }

    @Test
    void "replaces @variables with param values"() {
        String result = formatWithLang(
                'SELECT @variable, @a1_2.3$, @\'var name\', @"var name", @`var name`, @[var name], @\'var\\name\';',
                        [
                                "variable" : "\"variable value\"",
                                "a1_2.3\$" : "'weird value'",
                                "var name" : "'var value'",
                                "var\\name": "'var\\ value'"
                        ])
        expect(result).toBe(
                "SELECT\n" +
                        "  \"variable value\",\n" +
                        "  'weird value',\n" +
                        "  'var value',\n" +
                        "  'var value',\n" +
                        "  'var value',\n" +
                        "  'var value',\n" +
                        "  'var\\ value';"
        )
    }

    @Test
    void "recognizes :variables"() {
        String result = formatWithLang(
                'SELECT :variable, :a1_2.3$, :\'var name\', :"var name", :`var name`, :[var name];'
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  :variable,\n" +
                        "  :a1_2.3\$,\n" +
                        "  :'var name',\n" +
                        "  :\"var name\",\n" +
                        "  :`var name`,\n" +
                        "  :[var name];"
        )
    }

    @Test
    void "replaces :variables with param values"() {
        String result = formatWithLang(
                "SELECT :variable, :a1_2.3\$, :'var name', :\"var name\", :`var name`," +
                        " :[var name], :'escaped \\'var\\'', :\"^*& weird \\\" var   \";",
                        [
                                "variable"           : "\"variable value\"",
                                "a1_2.3\$"           : "'weird value'",
                                "var name"           : "'var value'",
                                "escaped 'var'"      : "'weirder value'",
                                "^*& weird \" var   ": "'super weird value'"
                        ])
        expect(result).toBe(
                "SELECT\n" +
                        "  \"variable value\",\n" +
                        "  'weird value',\n" +
                        "  'var value',\n" +
                        "  'var value',\n" +
                        "  'var value',\n" +
                        "  'var value',\n" +
                        "  'weirder value',\n" +
                        "  'super weird value';"
        )
    }

    @Test
    void "recognizes indexed placeholders"() {
        String result = formatWithLang("SELECT ?1, ?25, ?;")
        expect(result).toBe(
                "SELECT\n" +
                        "  ?1,\n" +
                        "  ?25,\n" +
                        "  ?;"
        )
    }

    @Test
    void "replaces ? numbered placeholders with param values"() {
        String result = formatWithLang("SELECT ?1, ?2, ?0;", ['0': "first", '1': "second", '2': "third"])

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
    void "formats query with GO batch separator"() {
        String result = formatWithLang("SELECT 1 GO SELECT 2")
        expect(result).toBe(
                "SELECT\n" +
                        "  1\n" +
                        "GO\n" +
                        "SELECT\n" +
                        "  2"
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
    void "formats simple SELECT with national characters (MSSQL)"() {
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
    void "formats FETCH FIRST like LIMIT"() {
        String result = formatWithLang(
                "SELECT * FETCH FIRST 2 ROWS ONLY;"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  *\n" +
                        "FETCH FIRST\n" +
                        "  2 ROWS ONLY;"
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

    @Test
    void "recognizes lowercase CASE END"() {
        String result = formatWithLang(
                "case when option = 'foo' then 1 else 2 end;"
        )

        expect(result).toBe(
                "case\n" +
                        "  when option = 'foo' then 1\n" +
                        "  else 2\n" +
                        "end;"
        )
    }

    // Regression test for issue #43
    @Test
    void "ignores words CASE and END inside other strings"() {
        String result = formatWithLang(
                "SELECT CASEDATE, ENDDATE FROM table1;"
        )

        expect(result).toBe(
                "SELECT\n" +
                        "  CASEDATE,\n" +
                        "  ENDDATE\n" +
                        "FROM\n" +
                        "  table1;"
        )
    }

    @Test
    void 'properly converts to uppercase in case statements'() {
        String result = formatWithLang(
            "case toString(getNumber()) when 'one' then 1 when 'two' then 2 when 'three' then 3 else 4 end;",
            FormatConfig.builder().uppercase(true).indent('        ').build()
        );
        expect(result).toBe(
         '''|CASE
            |        toString(getNumber())
            |        WHEN 'one' THEN 1
            |        WHEN 'two' THEN 2
            |        WHEN 'three' THEN 3
            |        ELSE 4
            |END;'''.stripMargin());
    }

    @Test
    void "formats tricky line comments"() {
        expect(formatWithLang("SELECT a#comment, here\nFROM b--comment")).toBe(
                "SELECT\n" +
                        "  a #comment, here\n" +
                        "FROM\n" +
                        "  b --comment"
        )
    }

    @Test
    void "formats line comments followed by semicolon"() {
        expect(formatWithLang("SELECT a FROM b\n--comment\n;")).toBe(
                "SELECT\n" +
                        "  a\n" +
                        "FROM\n" +
                        "  b --comment\n" +
                        ";"
        )
    }

    @Test
    void "formats line comments followed by comma"() {
        expect(formatWithLang("SELECT a --comment\n, b")).toBe(
                "SELECT\n" +
                        "  a --comment\n" +
                        ",\n" +
                        "  b"
        )
    }

    @Test
    void "formats line comments followed by close-paren"() {
        expect(formatWithLang("SELECT ( a --comment\n )")).toBe(
                "SELECT\n" +
                        "  (a --comment\n" +
                        ")"
        )
    }

    @Test
    void "formats line comments followed by open-paren"() {
        expect(formatWithLang("SELECT a --comment\n()")).toBe(
                "SELECT\n" +
                        "  a --comment\n" +
                        "  ()"
        )
    }

    @Test
    void "formats lonely semicolon"() {
        expect(formatWithLang(";")).toBe(";")
    }

}
