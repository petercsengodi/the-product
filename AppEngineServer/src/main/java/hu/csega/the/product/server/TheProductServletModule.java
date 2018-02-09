package hu.csega.the.product.server;

import com.google.inject.servlet.ServletModule;

public class TheProductServletModule extends ServletModule {
	
	@Override
	protected void configureServlets() {
		serve("/helloWorld").with(HelloWorldServlet.class);
	}
	
}
