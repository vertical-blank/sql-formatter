package com.github.vertical_blank.sqlformatter.features

import com.github.vertical_blank.sqlformatter.core.FormatConfig
import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.*

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.amshove.kluent.*

import org.spekframework.spek2.style.specification.Suite

fun Suite.supportsAlterTable(formatter: SqlFormatter.Formatter) { with(formatter) {
  it("formats ALTER TABLE ... ALTER COLUMN query") {
    val result = format("ALTER TABLE supplier ALTER COLUMN supplier_name VARCHAR(100) NOT NULL;");
    expect(result).toBe("""
      ALTER TABLE
        supplier
      ALTER COLUMN
        supplier_name VARCHAR(100) NOT NULL;
    """.trimIndent());
  }
}}
