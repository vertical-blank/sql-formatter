package com.github.vertical_blank.sqlformatter

import org.spekframework.spek2.style.specification.Suite

fun Suite.behavesLikeSqlFormatter(formatter: SqlFormatter.Formatter) {
  with(formatter) {
    it("does nothing with empty input") {
      val result = format("")

      expect(result).toBe("")
    }

    it("formats lonely semicolon") { expect(format(";")).toBe(";") }

    it("formats simple SELECT query") {
      val result = format("SELECT count(*),Column1 FROM Table1;")
      expect(result)
          .toBe(
              """
      SELECT
        count(*),
        Column1
      FROM
        Table1;
                """.trimIndent())
    }

    it("formats complex SELECT") {
      val result =
          format(
              "SELECT DISTINCT name, ROUND(age/7) field1, 18 + 20 AS field2, 'some string' FROM foo;")
      expect(result)
          .toBe(
              """
      SELECT
        DISTINCT name,
        ROUND(age / 7) field1,
        18 + 20 AS field2,
        'some string'
      FROM
        foo;
                """.trimIndent())
    }

    it("formats SELECT with complex WHERE") {
      val result =
          format(
              """
      SELECT * FROM foo WHERE Column1 = 'testing'
      AND ( (Column2 = Column3 OR Column4 >= NOW()) );
    """)
      expect(result)
          .toBe(
              """
      SELECT
        *
      FROM
        foo
      WHERE
        Column1 = 'testing'
        AND (
          (
            Column2 = Column3
            OR Column4 >= NOW()
          )
        );
                """.trimIndent())
    }

    it("formats SELECT with top level reserved words") {
      val result =
          format(
              """
      SELECT * FROM foo WHERE name = 'John' GROUP BY some_column
      HAVING column > 10 ORDER BY other_column LIMIT 5;
    """)
      expect(result)
          .toBe(
              """
      SELECT
        *
      FROM
        foo
      WHERE
        name = 'John'
      GROUP BY
        some_column
      HAVING
        column > 10
      ORDER BY
        other_column
      LIMIT
        5;
                """.trimIndent())
    }

    it("formats LIMIT with two comma-separated values on single line") {
      val result = format("LIMIT 5, 10;")
      expect(result).toBe("""
      LIMIT
        5, 10;
                """.trimIndent())
    }

    it("formats LIMIT of single value followed by another SELECT using commas") {
      val result = format("LIMIT 5; SELECT foo, bar;")
      expect(result)
          .toBe(
              """
      LIMIT
        5;
      SELECT
        foo,
        bar;
                """.trimIndent())
    }

    it("formats LIMIT of single value and OFFSET") {
      val result = format("LIMIT 5 OFFSET 8;")
      expect(result).toBe("""
      LIMIT
        5 OFFSET 8;
                """.trimIndent())
    }

    it("recognizes LIMIT in lowercase") {
      val result = format("limit 5, 10;")
      expect(result).toBe("""
      limit
        5, 10;
                """.trimIndent())
    }

    it("preserves case of keywords") {
      val result = format("select distinct * frOM foo WHERe a > 1 and b = 3")
      expect(result)
          .toBe(
              """
      select
        distinct *
      frOM
        foo
      WHERe
        a > 1
        and b = 3
                """.trimIndent())
    }

    it("formats SELECT query with SELECT query inside it") {
      val result =
          format("""SELECT *, SUM(*) AS sum FROM (SELECT * FROM Posts LIMIT 30) WHERE a > b""")
      expect(result)
          .toBe(
              """
      SELECT
        *,
        SUM(*) AS sum
      FROM
        (
          SELECT
            *
          FROM
            Posts
          LIMIT
            30
        )
      WHERE
        a > b
                """.trimIndent())
    }

    it("formats simple INSERT query") {
      val result =
          format(
              """INSERT INTO Customers (ID, MoneyBalance, Address, City) VALUES (12,-123.4, 'Skagen 2111','Stv');""")
      expect(result)
          .toBe(
              """
      INSERT INTO
        Customers (ID, MoneyBalance, Address, City)
      VALUES
        (12, -123.4, 'Skagen 2111', 'Stv');
                """.trimIndent())
    }

    it("formats open paren after comma") {
      val result =
          format(
              """WITH TestIds AS (VALUES (4),(5), (6),(7),(9),(10),(11)) SELECT * FROM TestIds;""")
      expect(result)
          .toBe(
              """
      WITH TestIds AS (
        VALUES
          (4),
          (5),
          (6),
          (7),
          (9),
          (10),
          (11)
      )
      SELECT
        *
      FROM
        TestIds;
                """.trimIndent())
    }

    it("keeps short parenthesized list with nested parenthesis on single line") {
      val result = format("SELECT (a + b * (c - NOW()));")
      expect(result)
          .toBe("""
      SELECT
        (a + b * (c - NOW()));
                """.trimIndent())
    }

    it("breaks long parenthesized lists to multiple lines") {
      val result =
          format(
              """
      INSERT INTO some_table (id_product, id_shop, id_currency, id_country, id_registration) (
      SELECT IF(dq.id_discounter_shopping = 2, dq.value, dq.value / 100),
      IF (dq.id_discounter_shopping = 2, 'amount', 'percentage') FROM foo);
    """)
      expect(result)
          .toBe(
              """
      INSERT INTO
        some_table (
          id_product,
          id_shop,
          id_currency,
          id_country,
          id_registration
        ) (
          SELECT
            IF(
              dq.id_discounter_shopping = 2,
              dq.value,
              dq.value / 100
            ),
            IF (
              dq.id_discounter_shopping = 2,
              'amount',
              'percentage'
            )
          FROM
            foo
        );
                """.trimIndent())
    }

    it("formats simple UPDATE query") {
      val result =
          format(
              "UPDATE Customers SET ContactName='Alfred Schmidt', City='Hamburg' WHERE CustomerName='Alfreds Futterkiste';")
      expect(result)
          .toBe(
              """
      UPDATE
        Customers
      SET
        ContactName = 'Alfred Schmidt',
        City = 'Hamburg'
      WHERE
        CustomerName = 'Alfreds Futterkiste';
                """.trimIndent())
    }

    it("formats simple DELETE query") {
      val result = format("DELETE FROM Customers WHERE CustomerName='Alfred' AND Phone=5002132;")
      expect(result)
          .toBe(
              """
      DELETE FROM
        Customers
      WHERE
        CustomerName = 'Alfred'
        AND Phone = 5002132;
                """.trimIndent())
    }

    it("formats simple DROP query") {
      val result = format("DROP TABLE IF EXISTS admin_role;")
      expect(result).toBe("DROP TABLE IF EXISTS admin_role;")
    }

    it("formats incomplete query") {
      val result = format("SELECT count(")
      expect(result).toBe("""
      SELECT
        count(
                """.trimIndent())
    }

    it("formats UPDATE query with AS part") {
      val result =
          format(
              "UPDATE customers SET total_orders = order_summary.total  FROM ( SELECT * FROM bank) AS order_summary")
      expect(result)
          .toBe(
              """
      UPDATE
        customers
      SET
        total_orders = order_summary.total
      FROM
        (
          SELECT
            *
          FROM
            bank
        ) AS order_summary
                """.trimIndent())
    }

    it("formats top-level and newline multi-word reserved words with inconsistent spacing") {
      val result = format("SELECT * FROM foo LEFT \t   \n JOIN bar ORDER \n BY blah")
      expect(result)
          .toBe(
              """
      SELECT
        *
      FROM
        foo
        LEFT JOIN bar
      ORDER BY
        blah
                """.trimIndent())
    }

    it("formats long double parenthized queries to multiple lines") {
      val result = format("""((foo = '0123456789-0123456789-0123456789-0123456789'))""")
      expect(result)
          .toBe(
              """
      (
        (
          foo = '0123456789-0123456789-0123456789-0123456789'
        )
      )
                """.trimIndent())
    }

    it("formats short double parenthized queries to one line") {
      val result = format("((foo = 'bar'))")
      expect(result).toBe("((foo = 'bar'))")
    }

    it("formats logical operators") {
      expect(format("foo ALL bar")).toBe("foo ALL bar")
      expect(format("foo = ANY (1, 2, 3)")).toBe("foo = ANY (1, 2, 3)")
      expect(format("EXISTS bar")).toBe("EXISTS bar")
      expect(format("foo IN (1, 2, 3)")).toBe("foo IN (1, 2, 3)")
      expect(format("foo LIKE 'hello%'")).toBe("foo LIKE 'hello%'")
      expect(format("foo IS NULL")).toBe("foo IS NULL")
      expect(format("UNIQUE foo")).toBe("UNIQUE foo")
    }

    it("formats AND/OR operators") {
      expect(format("foo AND bar")).toBe("foo\nAND bar")
      expect(format("foo OR bar")).toBe("foo\nOR bar")
    }

    it("keeps separation between multiple statements") {
      expect(format("foo;bar;")).toBe("foo;\nbar;")
      expect(format("foo\n;bar;")).toBe("foo;\nbar;")
      expect(format("foo\n\n\n;bar;\n\n")).toBe("foo;\nbar;")

      val result =
          format(
              """
      SELECT count(*),Column1 FROM Table1;
      SELECT count(*),Column1 FROM Table2;
    """)
      expect(result)
          .toBe(
              """
      SELECT
        count(*),
        Column1
      FROM
        Table1;
      SELECT
        count(*),
        Column1
      FROM
        Table2;
                """.trimIndent())
    }

    it("formats unicode correctly") {
      val result = format("SELECT 结合使用, тест FROM table;")
      expect(result)
          .toBe(
              """
      SELECT
        结合使用,
        тест
      FROM
        table;
                """.trimIndent())
    }

    it("correctly indents create statement after select") {
      val result =
          format(
              """
      SELECT * FROM test;
      CREATE TABLE TEST(id NUMBER NOT NULL, col1 VARCHAR2(20), col2 VARCHAR2(20));
    """)
      expect(result)
          .toBe(
              """
      SELECT
        *
      FROM
        test;
      CREATE TABLE TEST(
        id NUMBER NOT NULL,
        col1 VARCHAR2(20),
        col2 VARCHAR2(20)
      );
                """.trimIndent())
    }

    it("correctly handles floats as single tokens") {
      val result = format("SELECT 1e-9 AS a, 1.5e-10 AS b, 3.5E12 AS c, 3.5e12 AS d;")
      expect(result)
          .toBe(
              """
      SELECT
        1e-9 AS a,
        1.5e-10 AS b,
        3.5E12 AS c,
        3.5e12 AS d;
                """.trimIndent())
    }

    it("does not split UNION ALL in half") {
      val result =
          format("""
      SELECT * FROM tbl1
      UNION ALL
      SELECT * FROM tbl2;
    """)
      expect(result)
          .toBe(
              """
      SELECT
        *
      FROM
        tbl1
      UNION ALL
      SELECT
        *
      FROM
        tbl2;
                """.trimIndent())
    }
  }
}
