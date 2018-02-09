package hu.csega.the.product.message.schema;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

public class MessageSchemaParser {

	private static final CharMatcher WHITESPACES = CharMatcher.whitespace();
	private static final CharMatcher OBJECTS = CharMatcher.anyOf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_");
	private static final CharMatcher OBJECT_START = CharMatcher.anyOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

	private static final Pattern OBJECT_NAME = Pattern.compile("^[A-Z][a-zA-Z0-9_]*$");
	private static final Pattern FIELD_NAME = Pattern.compile("^[a-z][a-zA-Z0-9_]*$");

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
		Map<String, MessageObjectType> objectTypes = schema.getObjects();
		jumpWhiteSpaces();

		while(pos < len) {
			String name = readObjectName();
			jumpWhiteSpaces();
			if(!OBJECT_NAME.matcher(name).matches())
				throw new MessageSchemaParsingException("Invalid field name: " + name);

			String block = readBlock();
			jumpWhiteSpaces();

			MessageObjectType objectType = parseObjectType(block);
			objectType.setName(name);
			objectTypes.put(name, objectType);
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

	private MessageObjectType parseObjectType(String block) throws MessageSchemaParsingException {
		MessageObjectType objectType = new MessageObjectType();
		if(block == null)
			return objectType;

		List<MessageObjectField> fields = objectType.getFields();

		Iterator<String> split = SPLITTER.split(block).iterator();
		while(split.hasNext()) {
			String part = split.next();
			if(Strings.isNullOrEmpty(part))
				continue;

			part = part.trim();
			MessageObjectField field = parseField(part);
			fields.add(field);
		}

		return objectType;
	}

	private MessageObjectField parseField(String part) throws MessageSchemaParsingException {
		int index = part.indexOf(':', 1);
		if(index < 0 || index == part.length() - 1)
			throw new MessageSchemaParsingException("Illegal object field description: " + part);

		String prefix = part.substring(0, index).trim();
		String suffix = part.substring(index + 1).trim();
		if(Strings.isNullOrEmpty(prefix) || Strings.isNullOrEmpty(suffix))
			throw new MessageSchemaParsingException("Illegal object field description: " + part);

		if(!FIELD_NAME.matcher(prefix).matches())
			throw new MessageSchemaParsingException("Illegal object field name: " + prefix);

		MessageFieldType type = parseExpression(suffix);

		MessageObjectField field = new MessageObjectField();
		field.setName(prefix);
		field.setType(type);
		return field;
	}

	private MessageFieldType parseExpression(String suffix) throws MessageSchemaParsingException {
		switch(suffix) {
		case "any":
			return MessageVariableType.ANY;
		case "boolean":
			return MessageVariableType.BOOLEAN;
		case "int":
			return MessageVariableType.INT;
		case "double":
			return MessageVariableType.DOUBLE;
		case "string":
			return MessageVariableType.STRING;
		default:
			break;
		}

		if(OBJECT_NAME.matcher(suffix).matches()) {
			MessageObjectType objectType = new MessageObjectType();
			objectType.setName(suffix);
			return objectType;
		}

		if(suffix.charAt(suffix.length() - 1) == ')') {
			int index = suffix.indexOf('(', 1);
			if(index > 0) {

				String typeName = suffix.substring(0, index).trim();
				String innerExpression = suffix.substring(index + 1, suffix.length() - 1).trim();

				if(typeName.equals("array")) {
					MessageFieldType innerType = parseExpression(innerExpression);
					MessageArrayType arrayType = new MessageArrayType();
					arrayType.setInnerType(innerType);
					return arrayType;
				} else if(typeName.equals("map")) {
					MessageFieldType innerType = parseExpression(innerExpression);
					MessageMapType mapType = new MessageMapType();
					mapType.setInnerType(innerType);
					return mapType;
				}
			}
		}

		throw new MessageSchemaParsingException("Invalid definition of field: " + suffix);
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
