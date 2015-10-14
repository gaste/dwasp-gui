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

package at.aau.dwaspgui.viewmodel;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import at.aau.dwaspgui.app.WindowManager;
import at.aau.dwaspgui.app.config.ApplicationPreferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class PreferencesViewModel implements ViewModel {
	private final WindowManager windowManager;
	private final StringProperty grounderCommand = new SimpleStringProperty(ApplicationPreferences.COMMAND_GROUNDER.get());
	private final StringProperty debuggerCommand = new SimpleStringProperty(ApplicationPreferences.COMMAND_DEBUGGER.get());
	
	@Inject
	public PreferencesViewModel(WindowManager windowManager) {
		this.windowManager = windowManager;
	}
	
	public StringProperty grounderCommand() { return grounderCommand; }
	public StringProperty debuggerCommand() { return debuggerCommand; }
	
	public void chooseGrounderCommand() {
		File f = windowManager.chooseFile();
		if (f != null && f.exists()) {
			grounderCommand.set(f.getAbsolutePath());
		}
	}
	
	public void chooseDebuggerCommand() {
		File f = windowManager.chooseFile();
		if (f != null && f.exists()) {
			debuggerCommand.set(f.getAbsolutePath());
		}
	}
	
	public void save() {
		ApplicationPreferences.COMMAND_DEBUGGER.set(debuggerCommand.get());
		ApplicationPreferences.COMMAND_GROUNDER.set(grounderCommand.get());
		windowManager.closeModalDialog(this);
	}
	
	public void cancel() {
		windowManager.closeModalDialog(this);
	}

	@Override
	public String getTitle() {
		return "DWASP - Settings";
	}

	@Override
	public List<Image> getIcons() {
		return Arrays.asList(
			new Image(WindowManager.class.getResourceAsStream("settings-icon.png")));
	}
}
