package com.github.vertical_blank.sqlformatter


import com.github.vertical_blank.sqlformatter.languages.AbstractFormatter
import groovy.transform.TypeChecked
import org.junit.jupiter.api.Test

@TypeChecked
abstract class FormatterTestBase {

    abstract String lang()

    static class Helper {
        String result

        Helper(String result) {
            this.result = result
        }

        def toBe(String expect) {
            assert this.result == expect
        }
    }

    static Helper expect(String result) {
        new Helper(result)
    }

    private AbstractFormatter getFormatter() {
        SqlFormatter.of(this.lang())
    }

    String formatWithLang(String query) {
        getFormatter().format(query)
    }

    String formatWithLang(String query, List<?> params) {
        getFormatter().format(query, params)
    }

    String formatWithLang(String query, Map<String, ?> params) {
        getFormatter().format(query, params)
    }

    @Test
    void "uses given indent config for indention"() {
        String result = SqlFormatter.format(
                "SELECT count(*),Column1 FROM Table1;",
                "    "
        )

        expect(result).toBe(
                "SELECT\n" +
                        "    count(*),\n" +
                        "    Column1\n" +
                        "FROM\n" +
                        "    Table1;"
        )
    }

    @Test
    void "formats simple SET SCHEMA queries"() {
        String result = formatWithLang("SET SCHEMA tetrisdb; SET CURRENT SCHEMA bingodb;")
        expect(result).toBe(
                "SET SCHEMA\n" +
                        "  tetrisdb;\n" +
                        "SET CURRENT SCHEMA\n" +
                        "  bingodb;"
        )
    }

    @Test
    void "formats simple SELECT query"() {
        String result = formatWithLang("SELECT count(*),Column1 FROM Table1;")
        expect(result).toBe(
                "SELECT\n" +
                        "  count(*),\n" +
                        "  Column1\n" +
                        "FROM\n" +
                        "  Table1;"
        )
    }

    @Test
    void "formats complex SELECT"() {
        String result = formatWithLang(
                "SELECT DISTINCT name, ROUND(age/7) field1, 18 + 20 AS field2, 'some string' FROM foo;"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  DISTINCT name,\n" +
                        "  ROUND(age / 7) field1,\n" +
                        "  18 + 20 AS field2,\n" +
                        "  'some string'\n" +
                        "FROM\n" +
                        "  foo;"
        )
    }

    @Test
    void "formats SELECT with complex WHERE"() {
        String result = SqlFormatter.format(
                "SELECT * FROM foo WHERE Column1 = 'testing'" +
                        "AND ( (Column2 = Column3 OR Column4 >= NOW()) )"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  *\n" +
                        "FROM\n" +
                        "  foo\n" +
                        "WHERE\n" +
                        "  Column1 = 'testing'\n" +
                        "  AND (\n" +
                        "    (\n" +
                        "      Column2 = Column3\n" +
                        "      OR Column4 >= NOW()\n" +
                        "    )\n" +
                        "  )"
        )
    }

    @Test
    void "formats SELECT with toplevel reserved words"() {
        String result = formatWithLang(
                "SELECT * FROM foo WHERE name = 'John' GROUP BY some_column " +
                        "HAVING column > 10 ORDER BY other_column LIMIT 5;"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  *\n" +
                        "FROM\n" +
                        "  foo\n" +
                        "WHERE\n" +
                        "  name = 'John'\n" +
                        "GROUP BY\n" +
                        "  some_column\n" +
                        "HAVING\n" +
                        "  column > 10\n" +
                        "ORDER BY\n" +
                        "  other_column\n" +
                        "LIMIT\n" +
                        "  5;"
        )
    }

    @Test
    void "formats LIMIT with two comma-separated values on single line"() {
        String result = formatWithLang(
                "LIMIT 5, 10;"
        )
        expect(result).toBe(
                "LIMIT\n" +
                        "  5, 10;"
        )
    }

    @Test
    void "formats LIMIT of single value followed by another SELECT using commas"() {
        String result = formatWithLang(
                "LIMIT 5; SELECT foo, bar;"
        )
        expect(result).toBe(
                "LIMIT\n" +
                        "  5;\n" +
                        "SELECT\n" +
                        "  foo,\n" +
                        "  bar;"
        )
    }

    @Test
    void "formats LIMIT of single value and OFFSET"() {
        String result = formatWithLang(
                "LIMIT 5 OFFSET 8;"
        )
        expect(result).toBe(
                "LIMIT\n" +
                        "  5 OFFSET 8;"
        )
    }

    @Test
    void "recognizes LIMIT in lowercase"() {
        String result = formatWithLang(
                "limit 5, 10;"
        )
        expect(result).toBe(
                "limit\n" +
                        "  5, 10;"
        )
    }

    @Test
    void "preserves case of keywords"() {
        String result = formatWithLang(
                "select distinct * frOM foo left join bar WHERe a > 1 and b = 3"
        )
        expect(result).toBe(
                "select\n" +
                        "  distinct *\n" +
                        "frOM\n" +
                        "  foo\n" +
                        "  left join bar\n" +
                        "WHERe\n" +
                        "  a > 1\n" +
                        "  and b = 3"
        )
    }

    @Test
    void "formats SELECT query with SELECT query inside it"() {
        String result = formatWithLang(
                "SELECT *, SUM(*) AS sum FROM (SELECT * FROM Posts LIMIT 30) WHERE a > b"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  *,\n" +
                        "  SUM(*) AS sum\n" +
                        "FROM\n" +
                        "  (\n" +
                        "    SELECT\n" +
                        "      *\n" +
                        "    FROM\n" +
                        "      Posts\n" +
                        "    LIMIT\n" +
                        "      30\n" +
                        "  )\n" +
                        "WHERE\n" +
                        "  a > b"
        )
    }

    @Test
    void "formats SELECT query with INNER JOIN"() {
        String result = formatWithLang(
                "SELECT customer_id.from, COUNT(order_id) AS total FROM customers " +
                        "INNER JOIN orders ON customers.customer_id = orders.customer_id;"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  customer_id.from,\n" +
                        "  COUNT(order_id) AS total\n" +
                        "FROM\n" +
                        "  customers\n" +
                        "  INNER JOIN orders ON customers.customer_id = orders.customer_id;"
        )
    }

    @Test
    void "formats SELECT query with different comments"() {
        String result = formatWithLang(
                "SELECT\n" +
                        "/*\n" +
                        " * This is a block comment\n" +
                        " */\n" +
                        "* FROM\n" +
                        "-- This is another comment\n" +
                        "MyTable # One final comment\n" +
                        "WHERE 1 = 2;"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  /*\n" +
                        "   * This is a block comment\n" +
                        "   */\n" +
                        "  *\n" +
                        "FROM\n" +
                        "  -- This is another comment\n" +
                        "  MyTable # One final comment\n" +
                        "WHERE\n" +
                        "  1 = 2;"
        )
    }

    @Test
    void "formats simple INSERT query"() {
        String result = formatWithLang(
                "INSERT INTO Customers (ID, MoneyBalance, Address, City) VALUES (12,-123.4, 'Skagen 2111','Stv')"
        )
        expect(result).toBe(
                "INSERT INTO\n" +
                        "  Customers (ID, MoneyBalance, Address, City)\n" +
                        "VALUES\n" +
                        "  (12, -123.4, 'Skagen 2111', 'Stv')"
        )
    }

    @Test
    void "keeps short parenthized list with nested parenthesis on single line"() {
        String result = formatWithLang(
                "SELECT (a + b * (c - NOW()))"
        )
        expect(result).toBe(
                "SELECT\n" +
                        "  (a + b * (c - NOW()))"
        )
    }

    @Test
    void "breaks long parenthized lists to multiple lines"() {
        String result = formatWithLang(
                "INSERT INTO some_table (id_product, id_shop, id_currency, id_country, id_registration) (" +
                        "SELECT IF(dq.id_discounter_shopping = 2, dq.value, dq.value / 100)," +
                        "IF (dq.id_discounter_shopping = 2, 'amount', 'percentage') FROM foo)"
        )
        expect(result).toBe(
                "INSERT INTO\n" +
                        "  some_table (\n" +
                        "    id_product,\n" +
                        "    id_shop,\n" +
                        "    id_currency,\n" +
                        "    id_country,\n" +
                        "    id_registration\n" +
                        "  ) (\n" +
                        "    SELECT\n" +
                        "      IF(\n" +
                        "        dq.id_discounter_shopping = 2,\n" +
                        "        dq.value,\n" +
                        "        dq.value / 100\n" +
                        "      ),\n" +
                        "      IF (\n" +
                        "        dq.id_discounter_shopping = 2,\n" +
                        "        'amount',\n" +
                        "        'percentage'\n" +
                        "      )\n" +
                        "    FROM\n" +
                        "      foo\n" +
                        "  )"
        )
    }

    @Test
    void "formats simple UPDATE query"() {
        String result = formatWithLang(
                "UPDATE Customers SET ContactName='Alfred Schmidt', City='Hamburg' WHERE CustomerName='Alfreds Futterkiste';"
        )
        expect(result).toBe(
                "UPDATE\n" +
                        "  Customers\n" +
                        "SET\n" +
                        "  ContactName = 'Alfred Schmidt',\n" +
                        "  City = 'Hamburg'\n" +
                        "WHERE\n" +
                        "  CustomerName = 'Alfreds Futterkiste';"
        )
    }

    @Test
    void "formats simple DELETE query"() {
        String result = formatWithLang(
                "DELETE FROM Customers WHERE CustomerName='Alfred' AND Phone=5002132;"
        )
        expect(result).toBe(
                "DELETE FROM\n" +
                        "  Customers\n" +
                        "WHERE\n" +
                        "  CustomerName = 'Alfred'\n" +
                        "  AND Phone = 5002132;"
        )
    }

    @Test
    void "formats simple DROP query"() {
        String result = formatWithLang(
                "DROP TABLE IF EXISTS admin_role;"
        )
        expect(result).toBe(
                "DROP TABLE IF EXISTS admin_role;"
        )
    }

    @Test
    void "formats uncomplete query"() {
        String result = formatWithLang("SELECT count(")
        expect(result).toBe(
                "SELECT\n" +
                        "  count("
        )
    }

    @Test
    void "formats query that ends with open comment"() {
        String result = formatWithLang("SELECT count(*)\n/*Comment")
        expect(result).toBe(
                "SELECT\n" +
                        "  count(*)\n" +
                        "  /*Comment"
        )
    }

    @Test
    void "formats UPDATE query with AS part"() {
        String result = formatWithLang(
                "UPDATE customers SET totalorders = ordersummary.total  FROM ( SELECT * FROM bank) AS ordersummary"
        )
        expect(result).toBe(
                "UPDATE\n" +
                        "  customers\n" +
                        "SET\n" +
                        "  totalorders = ordersummary.total\n" +
                        "FROM\n" +
                        "  (\n" +
                        "    SELECT\n" +
                        "      *\n" +
                        "    FROM\n" +
                        "      bank\n" +
                        "  ) AS ordersummary"
        )
    }

    @Test
    void "formats top-level and newline multi-word reserved words with inconsistent spacing"() {
        String result = formatWithLang("SELECT * FROM foo LEFT \t OUTER  \n JOIN bar ORDER \n BY blah")
        expect(result).toBe(
                "SELECT\n" +
                        "  *\n" +
                        "FROM\n" +
                        "  foo\n" +
                        "  LEFT OUTER JOIN bar\n" +
                        "ORDER BY\n" +
                        "  blah"
        )
    }

    @Test
    void "formats long double parenthized queries to multiple lines"() {
        String result = formatWithLang("((foo = '0123456789-0123456789-0123456789-0123456789'))")
        expect(result).toBe(
                "(\n" +
                        "  (\n" +
                        "    foo = '0123456789-0123456789-0123456789-0123456789'\n" +
                        "  )\n" +
                        ")"
        )
    }

    @Test
    void "formats short double parenthized queries to one line"() {
        String result = formatWithLang("((foo = 'bar'))")
        expect(result).toBe("((foo = 'bar'))")
    }

    @Test
    void "formats single-char operators"() {
        expect(formatWithLang("foo = bar")).toBe("foo = bar")
        expect(formatWithLang("foo < bar")).toBe("foo < bar")
        expect(formatWithLang("foo > bar")).toBe("foo > bar")
        expect(formatWithLang("foo + bar")).toBe("foo + bar")
        expect(formatWithLang("foo - bar")).toBe("foo - bar")
        expect(formatWithLang("foo * bar")).toBe("foo * bar")
        expect(formatWithLang("foo / bar")).toBe("foo / bar")
        expect(formatWithLang("foo % bar")).toBe("foo % bar")
    }

    @Test
    void "formats multi-char operators"() {
        expect(formatWithLang("foo != bar")).toBe("foo != bar")
        expect(formatWithLang("foo <> bar")).toBe("foo <> bar")
        expect(formatWithLang("foo == bar")).toBe("foo == bar") // N1QL
        expect(formatWithLang("foo || bar")).toBe("foo || bar") // Oracle, Postgres, N1QL string concat

        expect(formatWithLang("foo <= bar")).toBe("foo <= bar")
        expect(formatWithLang("foo >= bar")).toBe("foo >= bar")

        expect(formatWithLang("foo !< bar")).toBe("foo !< bar")
        expect(formatWithLang("foo !> bar")).toBe("foo !> bar")
    }

    @Test
    void "formats logical operators"() {
        expect(formatWithLang("foo ALL bar")).toBe("foo ALL bar")
        expect(formatWithLang("foo = ANY (1, 2, 3)")).toBe("foo = ANY (1, 2, 3)")
        expect(formatWithLang("EXISTS bar")).toBe("EXISTS bar")
        expect(formatWithLang("foo IN (1, 2, 3)")).toBe("foo IN (1, 2, 3)")
        expect(formatWithLang("foo LIKE 'hello%'")).toBe("foo LIKE 'hello%'")
        expect(formatWithLang("foo IS NULL")).toBe("foo IS NULL")
        expect(formatWithLang("UNIQUE foo")).toBe("UNIQUE foo")
    }

    @Test
    void "formats AND OR operators"() {
        expect(formatWithLang("foo BETWEEN bar AND baz")).toBe("foo BETWEEN bar\nAND baz")
        expect(formatWithLang("foo AND bar")).toBe("foo\nAND bar")
        expect(formatWithLang("foo OR bar")).toBe("foo\nOR bar")
    }

    @Test
    void "recognizes strings"() {
        expect(formatWithLang("\"foo JOIN bar\"")).toBe("\"foo JOIN bar\"")
        expect(formatWithLang("'foo JOIN bar'")).toBe("'foo JOIN bar'")
        expect(formatWithLang("`foo JOIN bar`")).toBe("`foo JOIN bar`")
    }

    @Test
    void "recognizes escaped strings"() {
        expect(formatWithLang("\"foo \\\" JOIN bar\"")).toBe("\"foo \\\" JOIN bar\"")
        expect(formatWithLang("'foo \\' JOIN bar'")).toBe("'foo \\' JOIN bar'")
        expect(formatWithLang("`foo `` JOIN bar`")).toBe("`foo `` JOIN bar`")
    }

    @Test
    void "formats postgres specific operators"() {
        expect(formatWithLang("column::int")).toBe("column :: int")
        expect(formatWithLang("v->2")).toBe("v -> 2")
        expect(formatWithLang("v->>2")).toBe("v ->> 2")
        expect(formatWithLang("foo ~~ 'hello'")).toBe("foo ~~ 'hello'")
        expect(formatWithLang("foo !~ 'hello'")).toBe("foo !~ 'hello'")
        expect(formatWithLang("foo ~* 'hello'")).toBe("foo ~* 'hello'")
        expect(formatWithLang("foo ~~* 'hello'")).toBe("foo ~~* 'hello'")
        expect(formatWithLang("foo !~~ 'hello'")).toBe("foo !~~ 'hello'")
        expect(formatWithLang("foo !~* 'hello'")).toBe("foo !~* 'hello'")
        expect(formatWithLang("foo !~~* 'hello'")).toBe("foo !~~* 'hello'")
    }

    @Test
    void "keeps separation between multiple statements"() {
        expect(formatWithLang("foo;bar;")).toBe("foo;\nbar;")
        expect(formatWithLang("foo\n;bar;")).toBe("foo;\nbar;")
        expect(formatWithLang("foo\n\n\n;bar;\n\n")).toBe("foo;\nbar;")

        String result = formatWithLang("SELECT count(*),Column1 FROM Table1;\nSELECT count(*),Column1 FROM Table2;")
        expect(result).toBe(
                "SELECT\n" +
                        "  count(*),\n" +
                        "  Column1\n" +
                        "FROM\n" +
                        "  Table1;\n" +
                        "SELECT\n" +
                        "  count(*),\n" +
                        "  Column1\n" +
                        "FROM\n" +
                        "  Table2;"
        )
    }



    @Test
    void 'recognizes line-comments with Windows line-endings' () {
        String result = formatWithLang('SELECT * FROM\r\n-- line comment 1\r\nMyTable -- line comment 2\r\n');
        println result
        expect(result).toBe('SELECT\n  *\nFROM\n  -- line comment 1\n  MyTable -- line comment 2');
    }

}
