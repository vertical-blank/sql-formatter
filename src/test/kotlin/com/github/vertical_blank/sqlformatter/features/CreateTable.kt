package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.expect
import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsCreateTable(formatter: SqlFormatter.Formatter) {
  with(formatter) {
    it("formats short CREATE TABLE") {
      expect(format("CREATE TABLE items (a INT PRIMARY KEY, b TEXT);"))
          .toBe("CREATE TABLE items (a INT PRIMARY KEY, b TEXT);")
    }

    // The decision to place it to multiple lines is made based on the length of text inside
    // BRACEs
    // ignoring the whitespace. (Which is not quite right :P)
    it("formats long CREATE TABLE") {
      expect(
              format(
                  "CREATE TABLE items (a INT PRIMARY KEY, b TEXT, c INT NOT NULL, doggie INT NOT NULL);"))
          .toBe(
              """
      CREATE TABLE items (
        a INT PRIMARY KEY,
        b TEXT,
        c INT NOT NULL,
        doggie INT NOT NULL
      );
                """.trimIndent())
    }
  }
}
