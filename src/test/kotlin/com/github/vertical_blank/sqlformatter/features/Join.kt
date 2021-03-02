package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.core.FormatConfig
import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.*
import com.github.vertical_blank.sqlformatter.enums.StringLiteral;

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.collections.emptyList
import kotlin.collections.listOf
import org.amshove.kluent.*

import java.util.regex.Pattern

import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsJoin(formatter: SqlFormatter.Formatter, without: List<String> = listOf(), additionally: List<String> = listOf()) { with(formatter) {
  val unsupportedJoinRegex = if(without.isNotEmpty()) without.joinToString("|") else "^whateve_!%&$";
  val isSupportedJoin = { join: String -> unsupportedJoinRegex.toRegex().find(join) == null };

  listOf("CROSS JOIN", "NATURAL JOIN").filter(isSupportedJoin).forEach{ join ->
    it("supports ${join}") {
      val result = format("SELECT * FROM tbl1 ${join} tbl2");
      expect(result).toBe("""
        SELECT
          *
        FROM
          tbl1
          ${join} tbl2
      """.trimIndent());
    }
  }

  // <join> ::= [ <join type> ] JOIN
  //
  // <join type> ::= INNER | <outer join type> [ OUTER ]
  //
  // <outer join type> ::= LEFT | RIGHT | FULL

  (listOf(
    "JOIN",
    "INNER JOIN",
    "LEFT JOIN",
    "LEFT OUTER JOIN",
    "RIGHT JOIN",
    "RIGHT OUTER JOIN",
    "FULL JOIN",
    "FULL OUTER JOIN",
  ) + additionally)
    .filter(isSupportedJoin)
    .forEach { join ->
      it("supports ${join}") {
        val result = format("""
          SELECT customer_id.from, COUNT(order_id) AS total FROM customers
          ${join} orders ON customers.customer_id = orders.customer_id;
        """.trimIndent());
        expect(result).toBe("""
          SELECT
            customer_id.from,
            COUNT(order_id) AS total
          FROM
            customers
            ${join} orders ON customers.customer_id = orders.customer_id;
        """.trimIndent());
      }
    }
}}
