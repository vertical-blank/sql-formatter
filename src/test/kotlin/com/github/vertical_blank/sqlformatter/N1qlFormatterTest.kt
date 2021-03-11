package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.features.supportsBetween
import com.github.vertical_blank.sqlformatter.features.supportsJoin
import com.github.vertical_blank.sqlformatter.features.supportsOperators
import com.github.vertical_blank.sqlformatter.features.supportsSchema
import com.github.vertical_blank.sqlformatter.features.supportsStrings
import com.github.vertical_blank.sqlformatter.languages.Dialect
import com.github.vertical_blank.sqlformatter.languages.StringLiteral
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object N1qlFormatterTest :
    Spek({
      val formatter = SqlFormatter.of(Dialect.N1ql)

      describe("N1qlFormatter") {
        with(formatter) {
          behavesLikeSqlFormatter(formatter)
          supportsStrings(
              formatter,
              listOf(
                  StringLiteral.DOUBLE_QUOTE, StringLiteral.SINGLE_QUOTE, StringLiteral.BACK_QUOTE))
          supportsBetween(formatter)
          supportsSchema(formatter)
          supportsOperators(formatter, listOf("%", "==", "!="))
          supportsJoin(formatter, without = listOf("FULL", "CROSS", "NATURAL"))

          it("formats SELECT query with element selection expression") {
            val result = format("SELECT order_lines[0].productId FROM orders;")
            expect(result)
                .toBe(
                    """
        SELECT
          order_lines[0].productId
        FROM
          orders;
                    """.trimIndent())
          }

          it("formats SELECT query with primary key querying") {
            val result = format("SELECT fname, email FROM tutorial USE KEYS ['dave', 'ian'];")
            expect(result)
                .toBe(
                    """
        SELECT
          fname,
          email
        FROM
          tutorial
        USE KEYS
          ['dave', 'ian'];
                    """.trimIndent())
          }

          it("formats INSERT with {} object literal") {
            val result =
                format("INSERT INTO heroes (KEY, VALUE) VALUES ('123', {'id':1,'type':'Tarzan'});")
            expect(result)
                .toBe(
                    """
        INSERT INTO
          heroes (KEY, VALUE)
        VALUES
          ('123', {'id': 1, 'type': 'Tarzan'});
                    """.trimIndent())
          }

          it("formats INSERT with large object and array literals") {
            val result =
                format(
                    """
        INSERT INTO heroes (KEY, VALUE) VALUES ('123', {'id': 1, 'type': 'Tarzan',
        'array': [123456789, 123456789, 123456789, 123456789, 123456789], 'hello': 'world'});
      """)
            expect(result)
                .toBe(
                    """
        INSERT INTO
          heroes (KEY, VALUE)
        VALUES
          (
            '123',
            {
              'id': 1,
              'type': 'Tarzan',
              'array': [
                123456789,
                123456789,
                123456789,
                123456789,
                123456789
              ],
              'hello': 'world'
            }
          );
                    """.trimIndent())
          }

          it("formats SELECT query with UNNEST top level reserver word") {
            val result = format("SELECT * FROM tutorial UNNEST tutorial.children c;")
            expect(result)
                .toBe(
                    """
        SELECT
          *
        FROM
          tutorial
        UNNEST
          tutorial.children c;
                    """.trimIndent())
          }

          it("formats SELECT query with NEST and USE KEYS") {
            val result =
                format(
                    """
        SELECT * FROM usr
        USE KEYS 'Elinor_33313792' NEST orders_with_users orders
        ON KEYS ARRAY s.order_id FOR s IN usr.shipped_order_history END;
      """)
            expect(result)
                .toBe(
                    """
        SELECT
          *
        FROM
          usr
        USE KEYS
          'Elinor_33313792'
        NEST
          orders_with_users orders ON KEYS ARRAY s.order_id FOR s IN usr.shipped_order_history END;
                    """.trimIndent())
          }

          it("formats explained DELETE query with USE KEYS and RETURNING") {
            val result = format("EXPLAIN DELETE FROM tutorial t USE KEYS 'baldwin' RETURNING t")
            expect(result)
                .toBe(
                    """
        EXPLAIN DELETE FROM
          tutorial t
        USE KEYS
          'baldwin' RETURNING t
                    """.trimIndent())
          }

          it("formats UPDATE query with USE KEYS and RETURNING") {
            val result =
                format(
                    "UPDATE tutorial USE KEYS 'baldwin' SET type = 'actor' RETURNING tutorial.type")
            expect(result)
                .toBe(
                    """
        UPDATE
          tutorial
        USE KEYS
          'baldwin'
        SET
          type = 'actor' RETURNING tutorial.type
                    """.trimIndent())
          }

          it("recognizes \$variables") {
            val result = format("SELECT \$variable, \$'var name', \$\"var name\", \$`var name`;")
            expect(result)
                .toBe(
                    """
        SELECT
          ${"$"}variable,
          ${"$"}'var name',
          ${"$"}"var name",
          ${"$"}`var name`;
                    """.trimIndent())
          }

          it("replaces \$variables with param values") {
            val result =
                format(
                    "SELECT \$variable, $'var name', \$\"var name\", \$`var name`;",
                    mapOf(
                        "variable" to """"variable value"""",
                        "var name" to "'var value'",
                    ))
            expect(result)
                .toBe(
                    """
        SELECT
          "variable value",
          'var value',
          'var value',
          'var value';
                    """.trimIndent())
          }

          it("replaces $ numbered placeholders with param values") {
            val result =
                format(
                    "SELECT \$1, \$2, \$0;",
                    mapOf(
                        "0" to "first",
                        "1" to "second",
                        "2" to "third",
                    ))
            expect(result)
                .toBe(
                    """
        SELECT
          second,
          third,
          first;
                    """.trimIndent())
          }
        }
      }
    })
