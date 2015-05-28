package at.aau.app;

import javafx.application.Application;
import javafx.stage.Stage;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Main class of the application.
 * 
 * @author Philip Gasteiger
 */
public class App extends Application {
	private static Injector injector;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Injector getInjector() {
		return injector;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeIoC(primaryStage);
	}
	
	private void initializeIoC(Stage primaryStage) {
		injector = Guice.createInjector(new IoCModule(primaryStage));
	}
}
