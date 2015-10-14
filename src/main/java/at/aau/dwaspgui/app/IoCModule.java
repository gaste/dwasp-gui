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

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import at.aau.dwaspgui.aspide.AspideNotifier;
import at.aau.dwaspgui.debugger.Debugger;
import at.aau.dwaspgui.debugger.DebuggerImpl;
import at.aau.dwaspgui.parser.ProjectParser;
import at.aau.dwaspgui.parser.ProjectParserXMLImpl;
import at.aau.dwaspgui.serializer.ProjectSerializer;
import at.aau.dwaspgui.serializer.ProjectSerializerXMLImpl;
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
		bind(ProjectParser.class).to(ProjectParserXMLImpl.class).in(Scopes.SINGLETON);
		bind(ProjectSerializer.class).to(ProjectSerializerXMLImpl.class).in(Scopes.SINGLETON);
		bind(Debugger.class).to(DebuggerImpl.class).in(Scopes.SINGLETON);
		bind(AspideNotifier.class).in(Scopes.SINGLETON);
	}
}
