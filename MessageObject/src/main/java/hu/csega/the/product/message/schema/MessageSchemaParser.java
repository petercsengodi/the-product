package hu.csega.the.product.message.schema;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

public class MessageSchemaParser {

	private static final CharMatcher WHITESPACES = CharMatcher.whitespace();
	private static final CharMatcher OBJECTS = CharMatcher.anyOf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_");
	private static final CharMatcher OBJECT_START = CharMatcher.anyOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	private static final CharMatcher PRIMITIVES = CharMatcher.anyOf("abcdefghijklmnopqrstuvwxyz");

	private static final Splitter SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

	private MessageObjectSchema schema;
	private String definition;
	private int pos;
	private int len;

	public static MessageObjectSchema parse(String definition) throws MessageSchemaParsingException {
		MessageSchemaParser parser = new MessageSchemaParser();
		parser.schema = new MessageObjectSchema();
		parser.definition = definition;
		parser.pos = 0;
		parser.len = (definition == null ? 0 : definition.length());

		parser.parse();

		return parser.schema;
	}

	private void parse() throws MessageSchemaParsingException {
		jumpWhiteSpaces();

		while(pos < len) {
			String name = readObjectName();
			jumpWhiteSpaces();
			String block = readBlock();
			jumpWhiteSpaces();
			System.out.print(block);
		}
	}

	private String readBlock() throws MessageSchemaParsingException {
		if(pos >= len || definition.charAt(pos) != '{')
			throw new MessageSchemaParsingException("Block start expected. Pos: " + pos);

		pos++;
		int start = pos;

		while(true) {
			if(pos >= len)
				throw new MessageSchemaParsingException("Unfinished block at end of file.");

			char c = definition.charAt(pos);
			if(c == '{')
				throw new MessageSchemaParsingException("Nested blocks are not enabled. Pos: " + pos);

			if(c == '}')
				return definition.substring(start, pos++);

			pos++;
		}
	}

	private String readObjectName() throws MessageSchemaParsingException {
		if(pos >= len || !OBJECT_START.matches(definition.charAt(pos)))
			throw new MessageSchemaParsingException("Object name expected; object names start with uppercase letters. Pos: " + pos);

		int start = pos;
		pos++;

		while(pos < len && OBJECTS.matches(definition.charAt(pos)))
			pos++;

		return definition.substring(start, pos);
	}

	private void jumpWhiteSpaces() {
		while(pos < len && WHITESPACES.matches(definition.charAt(pos)))
			pos++;
	}
}
