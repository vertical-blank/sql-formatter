package vblank;

import org.junit.Assert;
import org.junit.Test;
import vblank.sql_formatter.SqlFormatter;
import vblank.sql_formatter.core.Config;

public class SqlFormatterTest {

	@Test
	public void callFromJava() {
		String format = SqlFormatter.format("SELECT foo, bar, CASE baz WHEN 'one' THEN 1 WHEN 'two' THEN 2 ELSE 3 END FROM table");
		Assert.assertEquals(format, "SELECT\n" +
						"  foo,\n" +
						"  bar,\n" +
						"  CASE\n" +
						"    baz\n" +
						"    WHEN 'one' THEN 1\n" +
						"    WHEN 'two' THEN 2\n" +
						"    ELSE 3\n" +
						"  END\n" +
						"FROM\n" +
						"  table");
	}

	@Test
	public void callFromJavaWithConfig() {
		String format = SqlFormatter.format(
						"SELECT foo, bar, CASE baz WHEN 'one' THEN 1 WHEN 'two' THEN 2 ELSE 3 END FROM table",
						Config.builder().indent("    ").build());
		Assert.assertEquals(format, "SELECT\n" +
						"    foo,\n" +
						"    bar,\n" +
						"    CASE\n" +
						"        baz\n" +
						"        WHEN 'one' THEN 1\n" +
						"        WHEN 'two' THEN 2\n" +
						"        ELSE 3\n" +
						"    END\n" +
						"FROM\n" +
						"    table");
	}

}
