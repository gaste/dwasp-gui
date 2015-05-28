package at.aau.app;

import javafx.stage.Stage;

import com.google.inject.AbstractModule;

/**
 * Guice IoC module that determines the bindings.
 * 
 * @author Philip Gasteiger
 */
public class IoCModule extends AbstractModule {
	private final Stage primaryStage;
	
	public IoCModule(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	@Override
	protected void configure() {
		bind(WindowManager.class).toInstance(new WindowManager(primaryStage));
	}
}
