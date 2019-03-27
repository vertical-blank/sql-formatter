package vblank.core

import groovy.transform.TypeChecked
import org.junit.Test
import vblank.SqlFormatter

@TypeChecked
class N1qlFormatterTest extends FormatterTestBase {

    @Override
    String lang() {
        return 'n1ql'
    }

    @Test void "formats SELECT query with element selection expression"() {
        String result = SqlFormatter.format("SELECT orderlines[0].productId FROM orders;",
            langConfig()
        )
        expect(result).toBe(
            "SELECT\n" +
                "  orderlines[0].productId\n" +
                "FROM\n" +
                "  orders;"
        )
    }

    @Test void "formats SELECT query with primary key quering"() {
        String result = SqlFormatter.format(
            "SELECT fname, email FROM tutorial USE KEYS ['dave', 'ian'];",
            langConfig()
        )
        expect(result).toBe(
            "SELECT\n" +
                "  fname,\n" +
                "  email\n" +
                "FROM\n" +
                "  tutorial\n" +
                "USE KEYS\n" +
                "  ['dave', 'ian'];"
        )
    }

    @Test void "formats INSERT with {} object literal"() {
        String result = SqlFormatter.format(
            "INSERT INTO heroes (KEY, VALUE) VALUES ('123', {'id':1,'type':'Tarzan'});",
            langConfig()
        )
        expect(result).toBe(
            "INSERT INTO\n" +
                "  heroes (KEY, VALUE)\n" +
                "VALUES\n" +
                "  ('123', {'id': 1, 'type': 'Tarzan'});"
        )
    }

    @Test void "formats INSERT with large object and array literals"() {
        String result = SqlFormatter.format(
            "INSERT INTO heroes (KEY, VALUE) VALUES ('123', {'id': 1, 'type': 'Tarzan', " +
                "'array': [123456789, 123456789, 123456789, 123456789, 123456789], 'hello': 'world'});",
            langConfig()
        )
        expect(result).toBe(
            "INSERT INTO\n" +
                "  heroes (KEY, VALUE)\n" +
                "VALUES\n" +
                "  (\n" +
                "    '123',\n" +
                "    {\n" +
                "      'id': 1,\n" +
                "      'type': 'Tarzan',\n" +
                "      'array': [\n" +
                "        123456789,\n" +
                "        123456789,\n" +
                "        123456789,\n" +
                "        123456789,\n" +
                "        123456789\n" +
                "      ],\n" +
                "      'hello': 'world'\n" +
                "    }\n" +
                "  );"
        )
    }

    @Test void "formats SELECT query with UNNEST toplevel reserver word"() {
        String result = SqlFormatter.format(
            "SELECT * FROM tutorial UNNEST tutorial.children c;",
            langConfig()
        )
        expect(result).toBe(
            "SELECT\n" +
                "  *\n" +
                "FROM\n" +
                "  tutorial\n" +
                "UNNEST\n" +
                "  tutorial.children c;"
        )
    }

    @Test void "formats SELECT query with NEST and USE KEYS"() {
        String result = SqlFormatter.format(
            "SELECT * FROM usr " +
                "USE KEYS 'Elinor_33313792' NEST orders_with_users orders " +
                "ON KEYS ARRAY s.order_id FOR s IN usr.shipped_order_history END;",
            langConfig()
        )
        expect(result).toBe(
            "SELECT\n" +
                "  *\n" +
                "FROM\n" +
                "  usr\n" +
                "USE KEYS\n" +
                "  'Elinor_33313792'\n" +
                "NEST\n" +
                "  orders_with_users orders ON KEYS ARRAY s.order_id FOR s IN usr.shipped_order_history END;"
        )
    }

    @Test void "formats explained DELETE query with USE KEYS and RETURNING"() {
        String result = SqlFormatter.format(
            "EXPLAIN DELETE FROM tutorial t USE KEYS 'baldwin' RETURNING t",
            langConfig()
        )
        expect(result).toBe(
            "EXPLAIN DELETE FROM\n" +
                "  tutorial t\n" +
                "USE KEYS\n" +
                "  'baldwin' RETURNING t"
        )
    }

    @Test void "formats UPDATE query with USE KEYS and RETURNING"() {
        String result = SqlFormatter.format(
            "UPDATE tutorial USE KEYS 'baldwin' SET type = 'actor' RETURNING tutorial.type",
            langConfig()
        )
        expect(result).toBe(
            "UPDATE\n" +
                "  tutorial\n" +
                "USE KEYS\n" +
                "  'baldwin'\n" +
                "SET\n" +
                "  type = 'actor' RETURNING tutorial.type"
        )
    }

    @Test void "recognizes \$variables"() {
        String result = SqlFormatter.format(
            "SELECT \$variable, \$'var name', \$\"var name\", \$`var name`;",
            langConfig()
        )
        expect(result).toBe(
            "SELECT\n" +
                "  \$variable,\n" +
                "  \$'var name',\n" +
                "  \$\"var name\",\n" +
                "  \$`var name`;"
        )
    }

    @Test void "replaces \$variables with param values"() {
        String result = SqlFormatter.format(
            "SELECT \$variable, \$'var name', \$\"var name\", \$`var name`;",
            langConfig().toBuilder().params(Params.Holder.of([
                "variable": "\"variable value\"",
                "var name": "'var value'"
            ] )).build()
        )
        expect(result).toBe(
            "SELECT\n" +
                "  \"variable value\",\n" +
                "  'var value',\n" +
                "  'var value',\n" +
                "  'var value';"
        )
    }

    @Test void "replaces \$ numbered placeholders with param values"() {
        String result = SqlFormatter.format("SELECT \$1, \$2, \$0;",
            langConfig().toBuilder().params(Params.Holder.of([
                '0': "first",
                '1': "second",
                '2': "third"
            ] )).build()
        )
        expect(result).toBe(
            "SELECT\n" +
                "  second,\n" +
                "  third,\n" +
                "  first;"
        )
    }

}
