package hu.csega.the.product.loader;

import static hu.csega.the.product.library.Files.readAllBytes;

import java.io.File;

public class CustomClassLoader extends ClassLoader {

	public CustomClassLoader(ClassLoader parent, String folder) {
		super(parent);
		this.parent = parent;
		this.folder = folder;

		if(!this.folder.endsWith(File.separator))
			this.folder += File.separator;
	}

	public Class<?> loadClass(String fname, String cname) {

		try {
			return parent.loadClass(cname);
		} catch(ClassNotFoundException ex) {
			byte[] bytes = readAllBytes(fname);
			return defineClass(cname, bytes, 0, bytes.length);
		}
	}

	@Override
	protected Class<?> findClass(String cname) throws ClassNotFoundException {
		try {
			return super.findClass(cname);
		} catch(ClassNotFoundException ex) {
			String fname = folder + cname.replaceAll("\\.", File.separator) + ".class";
			if(!new File(fname).exists())
				throw new ClassNotFoundException(cname);

			return loadClass(fname, cname);
		}
	}


	private ClassLoader parent;
	private String folder;
}
