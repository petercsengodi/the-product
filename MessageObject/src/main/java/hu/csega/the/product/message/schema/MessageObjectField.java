package hu.csega.the.product.message.schema;

public class MessageObjectField {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MessageFieldType getType() {
		return type;
	}

	public void setType(MessageFieldType type) {
		this.type = type;
	}

	private String name;
	private MessageFieldType type;
}
