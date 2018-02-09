package hu.csega.the.product.message.schema;

import java.util.Map;

import com.google.common.collect.Maps;

public class MessageObjectSchema {

	public Map<String, MessageObjectType> getObjects() {
		return objects;
	}

	public void setObjects(Map<String, MessageObjectType> objects) {
		this.objects = objects;
	}

	private Map<String, MessageObjectType> objects = Maps.newHashMap();
}
