package at.aau.dwaspgui.app;

import com.google.inject.Guice;
import com.google.inject.Injector;

import at.aau.dwaspgui.debug.Debugger;
import at.aau.dwaspgui.viewmodel.RootViewModel;
import at.aau.input.InvalidOptionException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class of the application.
 * 
 * @author Philip Gasteiger
 */
public class App extends Application {
	private static Injector injector;
	
	private Options options;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Injector getInjector() {
		return injector;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			options = new Options(getParameters().getRaw().toArray(new String[getParameters().getRaw().size()]));
		} catch (InvalidOptionException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			return;
		}
		
		if (options.isPrintHelp()) {
			options.printHelp();
			System.exit(0);
			return;
		}
		
		initializeIoC(primaryStage);
		
		WindowManager windowManager = injector.getInstance(WindowManager.class);
		RootViewModel rootViewModel = injector.getInstance(RootViewModel.class);
		
		windowManager.show(rootViewModel);
		
		if (options.isProjectFileSpecified())
			rootViewModel.openProject(options.getProjectFile());
		
		if (options.isStartedFromAspide())
			rootViewModel.isAspideSessions().set(true);
	}
	
	@Override
	public void stop() throws Exception {
		Debugger debugger = injector.getInstance(Debugger.class);
		debugger.stopDebugger();
	}
	
	private void initializeIoC(Stage primaryStage) {
		injector = Guice.createInjector(new IoCModule(primaryStage));
	}
}
