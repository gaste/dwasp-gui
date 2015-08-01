package at.aau.dwaspgui.app;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import at.aau.dwaspgui.aspide.AspideNotifier;
import at.aau.dwaspgui.debug.Debugger;
import at.aau.dwaspgui.debug.DebuggerImpl;
import at.aau.dwaspgui.parser.XMLProjectParser;
import at.aau.dwaspgui.parser.XMLProjectParserImpl;
import at.aau.dwaspgui.viewmodel.RootViewModel;
import javafx.stage.Stage;

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
		bind(RootViewModel.class).in(Scopes.SINGLETON);
		bind(XMLProjectParser.class).to(XMLProjectParserImpl.class).in(Scopes.SINGLETON);
		bind(Debugger.class).to(DebuggerImpl.class).in(Scopes.SINGLETON);
		bind(AspideNotifier.class).in(Scopes.SINGLETON);
	}
}
