package com.github.vertical_blank.sqlformatter.languages;

import com.github.vertical_blank.sqlformatter.core.DialectConfig;
import com.github.vertical_blank.sqlformatter.enums.StringLiteral;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class N1qlFormatter extends AbstractFormatter {

	private static final List<String> reservedWords = Arrays.asList(
					"ALL", "ALTER", "ANALYZE", "AND", "ANY", "ARRAY", "AS", "ASC",
					"BEGIN", "BETWEEN", "BINARY", "BOOLEAN", "BREAK", "BUCKET", "BUILD", "BY",
					"CALL", "CASE", "CAST", "CLUSTER", "COLLATE", "COLLECTION", "COMMIT", "CONNECT", "CONTINUE", "CORRELATE", "COVER", "CREATE",
					"DATABASE", "DATASET", "DATASTORE", "DECLARE", "DECREMENT", "DELETE", "DERIVED", "DESC", "DESCRIBE", "DISTINCT", "DO", "DROP",
					"EACH", "ELEMENT", "ELSE", "END", "EVERY", "EXCEPT", "EXCLUDE", "EXECUTE", "EXISTS", "EXPLAIN",
					"FALSE", "FETCH", "FIRST", "FLATTEN", "FOR", "FORCE", "FROM", "FUNCTION",
					"GRANT", "GROUP", "GSI",
					"HAVING",
					"IF", "IGNORE", "ILIKE", "IN", "INCLUDE", "INCREMENT", "INDEX", "INFER", "INLINE", "INNER", "INSERT", "INTERSECT", "INTO", "IS",
					"JOIN",
					"KEY", "KEYS", "KEYSPACE", "KNOWN",
					"LAST", "LEFT", "LET", "LETTING", "LIKE", "LIMIT", "LSM",
					"MAP", "MAPPING", "MATCHED", "MATERIALIZED", "MERGE", "MINUS", "MISSING",
					"NAMESPACE", "NEST", "NOT", "NULL", "NUMBER",
					"OBJECT", "OFFSET", "ON", "OPTION", "OR", "ORDER", "OUTER", "OVER",
					"PARSE", "PARTITION", "PASSWORD", "PATH", "POOL", "PREPARE", "PRIMARY", "PRIVATE", "PRIVILEGE", "PROCEDURE", "PUBLIC",
					"RAW", "REALM", "REDUCE", "RENAME", "RETURN", "RETURNING", "REVOKE", "RIGHT", "ROLE", "ROLLBACK",
					"SATISFIES", "SCHEMA", "SELECT", "SELF", "SEMI", "SET", "SHOW", "SOME", "START", "STATISTICS", "STRING", "SYSTEM",
					"THEN", "TO", "TRANSACTION", "TRIGGER", "TRUE", "TRUNCATE",
					"UNDER", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UNSET", "UPDATE", "UPSERT", "USE", "USER", "USING",
					"VALIDATE", "VALUE", "VALUED", "VALUES", "VIA", "VIEW",
					"WHEN", "WHERE", "WHILE", "WITH", "WITHIN", "WORK",
					"XOR"
	);

	private static final List<String> reservedToplevelWords = Arrays.asList(
					"DELETE FROM",
					"EXCEPT ALL", "EXCEPT", "EXPLAIN DELETE FROM", "EXPLAIN UPDATE", "EXPLAIN UPSERT",
					"FROM",
					"GROUP BY",
					"HAVING",
					"INFER", "INSERT INTO", "INTERSECT ALL", "INTERSECT",
					"LET", "LIMIT",
					"MERGE",
					"NEST",
					"ORDER BY",
					"PREPARE",
					"SELECT", "SET CURRENT SCHEMA", "SET SCHEMA", "SET",
					"UNION ALL", "UNION", "UNNEST", "UPDATE", "UPSERT", "USE KEYS",
					"VALUES",
					"WHERE"
	);

	private static final List<String> reservedNewlineWords = Arrays.asList(
					"AND",
					"INNER JOIN",
					"JOIN",
					"LEFT JOIN",
					"LEFT OUTER JOIN",
					"OR", "OUTER JOIN",
					"RIGHT JOIN", "RIGHT OUTER JOIN",
					"XOR"
	);

	@Override
    DialectConfig dialectConfig() {
		return DialectConfig.builder()
						.reservedWords(reservedWords)
						.reservedToplevelWords(reservedToplevelWords)
						.reservedNewlineWords(reservedNewlineWords)
						.stringTypes(Arrays.asList(StringLiteral.DoubleQuote, StringLiteral.SingleQuote, StringLiteral.BackQuote))
						.openParens(Arrays.asList("(", "[", "{"))
						.closeParens(Arrays.asList(")", "]", "}"))
						.namedPlaceholderTypes(Collections.singletonList("$"))
						.lineCommentTypes(Arrays.asList("#", "--")).build();
	}

}
