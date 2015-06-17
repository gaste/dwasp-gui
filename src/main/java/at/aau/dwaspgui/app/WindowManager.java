package at.aau.dwaspgui.app;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.util.Messages;
import at.aau.dwaspgui.util.ViewLocator;
import at.aau.dwaspgui.view.AbstractView;
import at.aau.dwaspgui.viewmodel.ViewModel;

/**
 * Provides methods to show views on the main window as well as dialogs.
 * 
 * @author Philip Gasteiger
 */
public class WindowManager {
	private final Stage stage;
	private File lastFileChooserLocation = null;

	public WindowManager(Stage stage) {
		this.stage = stage;
		this.stage.setTitle("DWASP");
	}

	private void showScene(final Scene scene) {
		JFXUtil.runOnJFX(() -> {
			stage.setScene(scene);
			stage.show();
			stage.toFront();
		});
	}

	public void show(ViewModel viewModel) {
		AbstractView<?> view = ViewLocator.createView(viewModel);
		showScene(ViewLocator.loadScene(view));
	}
	
	public File chooseDirectory() {
		DirectoryChooser chooser = new DirectoryChooser();
		return chooser.showDialog(stage);
	}
	
	public File chooseFile() {
		FileChooser chooser = new FileChooser();
		
		if (lastFileChooserLocation != null) {
			chooser.setInitialDirectory(lastFileChooserLocation);
		}
		
		File chosenFile = chooser.showOpenDialog(stage);
		
		if (chosenFile != null) {
			lastFileChooserLocation = chosenFile.getParentFile();
		}
		
		return chosenFile;
	}
	
	public void showErrorDialog(Messages message, Exception details, Object... arguments) {
		// TODO implement showing dialogs
		System.out.println(message.format(arguments));
	}
}
