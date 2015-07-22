package at.aau.dwaspgui.app;

import it.unical.mat.aspide.closed.gui.debug2.AspideNotifier;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

import org.w3c.dom.Document;

import at.aau.dwaspgui.debug.Debugger;
import at.aau.dwaspgui.viewmodel.RootViewModel;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Main class of the application.
 * 
 * @author Philip Gasteiger
 */
public class App extends Application {
	private static Injector injector;
	
	private final AspideNotifier notifier;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Injector getInjector() {
		return injector;
	}
	
	public App() {
		this.notifier = null;
	}

	public App(AspideNotifier notifier) {
		this.notifier = notifier;
		
		launch();
	}
	
	public void openProject(Document project) {
		WindowManager windowManager = injector.getInstance(WindowManager.class);
		RootViewModel rootViewModel = injector.getInstance(RootViewModel.class);

		windowManager.show(rootViewModel);
		
		rootViewModel.openProject(project);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeIoC(primaryStage);
		
		WindowManager windowManager = injector.getInstance(WindowManager.class);
		RootViewModel rootViewModel = injector.getInstance(RootViewModel.class);
		
		windowManager.show(rootViewModel);

		List<String> params = getParameters().getRaw();
		
		if (params.size() == 1) {
			rootViewModel.openProject(new File(params.get(0)));
		}
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
