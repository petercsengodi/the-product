package hu.csega.the.product.message.schema;

import java.util.List;

import com.google.common.collect.Lists;

public class MessageObjectType implements MessageFieldType {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MessageObjectField> getFields() {
		return fields;
	}

	public void setFields(List<MessageObjectField> fields) {
		this.fields = fields;
	}

	private String name;
	private List<MessageObjectField> fields = Lists.newArrayList();
}
