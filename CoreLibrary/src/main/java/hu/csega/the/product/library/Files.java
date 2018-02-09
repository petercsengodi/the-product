package hu.csega.the.product.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.base.Charsets;

public class Files {

	public static String readAllString(String absolutePath) {
		return readAllString(absolutePath, Charsets.UTF_8);
	}

	public static String readAllString(String absolutePath, Charset charset) {
		return new String(readAllBytes(absolutePath), charset);
	}

	public static byte[] readAllBytes(String absolutePath) {
		File file = new File(absolutePath);
		int pos = 0, len = (int)(file.length()), block = 0;
		byte[] bytes = new byte[len];

		try (FileInputStream stream = new FileInputStream(file)) {
			while((block = stream.read(bytes, pos, len)) > -1) {
				pos += block;
				len -= block;
				if(len <= 0)
					break;
			}
		} catch(IOException ex) {
			throw new RuntimeException("Could not read file: " + absolutePath, ex);
		}

		return bytes;
	}

}
