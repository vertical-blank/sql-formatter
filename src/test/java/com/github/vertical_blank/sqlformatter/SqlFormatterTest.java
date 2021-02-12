package com.github.vertical_blank.sqlformatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.github.vertical_blank.sqlformatter.core.DialectConfig;
import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.core.Token;
import com.github.vertical_blank.sqlformatter.core.Tokenizer;
import com.github.vertical_blank.sqlformatter.core.util.JSLikeList;
import com.github.vertical_blank.sqlformatter.core.util.RegexUtil;
import com.github.vertical_blank.sqlformatter.core.util.Util;
import com.github.vertical_blank.sqlformatter.languages.StandardSqlFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SqlFormatterTest")
public class SqlFormatterTest {
	// @Test
	// void piyo() {
	// 	assertTrue(true);
	// }

	// @Nested
	// @DisplayName("SqlFormatterTest Nested Hoge")
	// class Hoge {
	// 	@Test
	// 	void piyo() {
	// 		assertTrue(true);
	// 	}
	// }

	// @Test
	// public void simple() {
	// 	String format = SqlFormatter
	// 			.format("SELECT foo, bar, CASE baz WHEN 'one' THEN 1 WHEN 'two' THEN 2 ELSE 3 END FROM table");
	// 	assertEquals(format, "SELECT\n" + "  foo,\n" + "  bar,\n" + "  CASE\n" + "    baz\n" + "    WHEN 'one' THEN 1\n"
	// 			+ "    WHEN 'two' THEN 2\n" + "    ELSE 3\n" + "  END\n" + "FROM\n" + "  table");
	// }

	// @Test
	// public void withIndent() {
	// 	String format = SqlFormatter
	// 			.format("SELECT foo, bar, CASE baz WHEN 'one' THEN 1 WHEN 'two' THEN 2 ELSE 3 END FROM table", "    ");
	// 	assertEquals(format,
	// 			"SELECT\n" + "    foo,\n" + "    bar,\n" + "    CASE\n" + "        baz\n" + "        WHEN 'one' THEN 1\n"
	// 					+ "        WHEN 'two' THEN 2\n" + "        ELSE 3\n" + "    END\n" + "FROM\n" + "    table");
	// }

	// @Test
	// public void withNamedParams() {
	// 	Map<String, String> namedParams = new HashMap<>();
	// 	namedParams.put("foo", "'bar'");

	// 	String format = SqlFormatter.format("SELECT * FROM tbl WHERE foo = @foo", namedParams);
	// 	assertEquals(format, "SELECT\n" + "  *\n" + "FROM\n" + "  tbl\n" + "WHERE\n" + "  foo = 'bar'");
	// }

	// @Test
	// public void withFatArrow() {
	// 	String format = SqlFormatter.format("SELECT * FROM tbl WHERE foo => '123'");
	// 	assertEquals(format, "SELECT\n" + "  *\n" + "FROM\n" + "  tbl\n" + "WHERE\n" + "  foo => '123'");
	// }

	// @Test
	// public void withIndexedParams() {
	// 	String format = SqlFormatter.format("SELECT * FROM tbl WHERE foo = ?", Arrays.asList("'bar'"));
	// 	assertEquals(format, "SELECT\n" + "  *\n" + "FROM\n" + "  tbl\n" + "WHERE\n" + "  foo = 'bar'");
	// }

	// @Test
	// public void hoge() {

	// 	String lineComment = RegexUtil.createLineCommentRegex(new JSLikeList<>(Arrays.asList("--")));
	// 	System.out.println(lineComment);

	// 	System.out.println(SqlFormatter.format(
	// 		"SELECT * FROM\r\n-- line comment 1\r\nMyTable -- line comment 2\r\n",
	// 		FormatConfig.builder().build())
	// 		.equals("SELECT\n  *\nFROM\n  -- line comment 1\r\n  MyTable -- line comment 2"));

	// 	// 
	// 	// SELECT\n  *\nFROM\n  -- line comment 1\r\n  MyTable -- line comment 2
	// 	// SELECT\n  *\nFROM\n  -- line comment 1\n  MyTable -- line comment 2

	// 	JSLikeList<Token> tokenize = new Tokenizer(
	// 		new StandardSqlFormatter(FormatConfig.builder().build()).dialectConfig())
	// 			.tokenize("SELECT * FROM\r\n-- line comment 1\r\nMyTable -- line comment 2\r\n");
	// 	tokenize.forEach(System.out::println);

	// 	// // String f = SqlFormatter.format("SELECT *, SUM(*) AS sum FROM (SELECT * FROM Posts LIMIT 30) WHERE a > b");
	// 	// // System.out.println(f);
		
	// 	// // String result = SqlFormatter.format("((foo = '0123456789-0123456789-0123456789-0123456789'))");

	// 	// JSLikeList<Token> tokenize = new Tokenizer(
	// 	// 	new StandardSqlFormatter(FormatConfig.builder().build()).dialectConfig()).tokenize("foo;\n\nbar;");
	// 	// tokenize.forEach(System.out::println);

	// 	// System.out.println(
	// 	// 	SqlFormatter.format("foo;\n\nbar;")
	// 	// );

	// }

}