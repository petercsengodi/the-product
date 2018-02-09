package hu.csega.the.product.message.schema;

public class MessageSchemaParsingException extends Exception {

	public MessageSchemaParsingException() {
	}

	public MessageSchemaParsingException(String message) {
		super(message);
	}

	public MessageSchemaParsingException(Throwable cause) {
		super(cause);
	}

	public MessageSchemaParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = 1L;
}
