package hu.csega.the.product.message.schema;

public class MessageArrayType implements MessageFieldType {

	public MessageFieldType getInnerType() {
		return innerType;
	}

	public void setInnerType(MessageFieldType innerType) {
		this.innerType = innerType;
	}

	private MessageFieldType innerType;
}
