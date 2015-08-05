package at.aau.dwaspgui.viewmodel;

import java.io.File;

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
	public Image getIcon() {
		return new Image(WindowManager.class.getResourceAsStream("settings-icon.png"));
	}
}
