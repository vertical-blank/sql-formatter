package vertical_blank.sql_formatter.core;

public class Token {
	public final TokenTypes type;
	public final String value;
	public final String regex;
	String key;

	Token(TokenTypes type, String value, String regex) {
		this.type = type;
		this.value = value;
		this.regex = regex;
	}

	Token(TokenTypes type, String value) {
		this(type, value, null);
	}

	@Override
	public String toString() {
		return "type: " + type + ", value: [" + value + "], regex: /" + regex + "/";
	}
}
