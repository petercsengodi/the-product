package hu.csega.the.product.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Tests {

	public static byte[] readAllBytes(Class<?> hostingClass, String filename) {
		try (InputStream stream = hostingClass.getResourceAsStream(filename)) {
			return readAllBytes(stream);
		} catch(IOException ex) {
			throw new RuntimeException("Could not read file: " + filename + " near class " + hostingClass.getName(), ex);
		}
	}

	public static byte[] readAllBytes(InputStream stream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;

		while((b = stream.read()) > -1)
			baos.write(b);

		return baos.toByteArray();
	}

}
