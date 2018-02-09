package hu.csega.the.product.message.schema;

import static org.junit.Assert.*;

import org.junit.Test;

import hu.csega.the.product.library.Files;

public class MessageSchemaParserTest {

	@Test
	public void test() throws Exception {
		String definition = Files.readAllString("src/test/resources/simple-message-schema.txt");
		assertNotNull(definition);
		System.out.println(definition);

		MessageObjectSchema schema = MessageSchemaParser.parse(definition);
	}

}
