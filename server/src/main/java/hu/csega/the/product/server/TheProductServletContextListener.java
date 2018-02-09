package hu.csega.the.product.server;

import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TheProductServletContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(
				new TheProductServletModule(), 
				new TheProductBusinessLogicModule()
			);
	}
}
