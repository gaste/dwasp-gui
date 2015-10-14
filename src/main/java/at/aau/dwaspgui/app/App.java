/*
 *  Copyright 2015 Philip Gasteiger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package at.aau.dwaspgui.app;

import com.google.inject.Guice;
import com.google.inject.Injector;

import at.aau.dwaspgui.app.config.ApplicationPreferences;
import at.aau.dwaspgui.debugger.Debugger;
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
		
		if (options.isDebuggerCommandSpecified()) {
			ApplicationPreferences.COMMAND_DEBUGGER.set(options.getDebuggerCommand());
		}
		
		if (options.isGrounderCommandSpecified()) {
			ApplicationPreferences.COMMAND_GROUNDER.set(options.getGrounderCommand());
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
