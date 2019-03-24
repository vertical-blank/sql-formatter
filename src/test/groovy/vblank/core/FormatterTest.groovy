package vblank.core

import org.junit.Test
import vblank.SqlFormatter

class FormatterTest {

    @Test
    void createTable() {
        def format = SqlFormatter.format("""CREATE TABLE items (a INT PRIMARY KEY, b TEXT, c INT NOT NULL, d INT NOT NULL);""")

        println format
    }

    @Test
    void 'uses given indent config for indention'() {
        assert SqlFormatter.format(
                "SELECT count(*),Column1 FROM Table1;",
                Tokenizer.Config.builder().indent("    ").build()) ==
                "SELECT\n" +
                "    count(*),\n" +
                "    Column1\n" +
                "FROM\n" +
                "    Table1;"
    }

    @Test
    void "formats long CREATE TABLE"() {
        SqlFormatter.format(
                "CREATE TABLE items (a INT PRIMARY KEY, b TEXT, c INT NOT NULL, d INT NOT NULL);"
        ) ==
                "CREATE TABLE items (\n" +
                "  a INT PRIMARY KEY,\n" +
                "  b TEXT,\n" +
                "  c INT NOT NULL,\n" +
                "  d INT NOT NULL\n" +
                ");"
    };

    @Test
    void "formats CASE ... WHEN inside SELECT"() {
        assert SqlFormatter.format(
                "SELECT foo, bar, CASE baz WHEN 'one' THEN 1 WHEN 'two' THEN 2 ELSE 3 END FROM table"
        ) ==
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
    };

    static expect(String result) {
        [toBe: { assert result == it }]
    }

    static format(String query) {
        SqlFormatter.format(query)
    }

    @Test
    void "formats complex SELECT"() {
        String result = format(
                "SELECT DISTINCT name, ROUND(age/7) field1, 18 + 20 AS field2, 'some string' FROM foo;"
        );
        expect(result).toBe(
                "SELECT\n" +
                        "  DISTINCT name,\n" +
                        "  ROUND(age / 7) field1,\n" +
                        "  18 + 20 AS field2,\n" +
                        "  'some string'\n" +
                        "FROM\n" +
                        "  foo;"
        );
    }

    @Test
    void "formats SELECT with complex WHERE"() {
        String result = format(
                "SELECT * FROM foo WHERE Column1 = 'testing'" +
                        "AND ( (Column2 = Column3 OR Column4 >= NOW()) );"
        );
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
                        "  );"
        );
    }

    @Test
    void "formats SELECT with toplevel reserved words"() {
        String result = format(
                "SELECT * FROM foo WHERE name = 'John' GROUP BY some_column " +
                        "HAVING column > 10 ORDER BY other_column LIMIT 5;"
        );
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
        );
    }


    @Test
    void "formats SELECT query with SELECT query inside it"() {
        String result = format(
                "SELECT *, SUM(*) AS sum FROM (SELECT * FROM Posts LIMIT 30) WHERE a > b"
        );
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
        );
    }

    @Test
    void "formats SELECT query with INNER JOIN"() {
        String result = format(
                "SELECT customer_id.from, COUNT(order_id) AS total FROM customers " +
                        "INNER JOIN orders ON customers.customer_id = orders.customer_id;"
        );
        expect(result).toBe(
                "SELECT\n" +
                        "  customer_id.from,\n" +
                        "  COUNT(order_id) AS total\n" +
                        "FROM\n" +
                        "  customers\n" +
                        "  INNER JOIN orders ON customers.customer_id = orders.customer_id;"
        );
    }

    @Test
    void "formats SELECT query with different comments"() {
        String result = format(
                "SELECT\n" +
                        "/*\n" +
                        " * This is a block comment\n" +
                        " */\n" +
                        "* FROM\n" +
                        "-- This is another comment\n" +
                        "MyTable # One final comment\n" +
                        "WHERE 1 = 2;"
        );
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
        );
    }


}
