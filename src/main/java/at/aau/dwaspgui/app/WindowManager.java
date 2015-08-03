package at.aau.dwaspgui.app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.util.Messages;
import at.aau.dwaspgui.util.ViewLocator;
import at.aau.dwaspgui.view.AbstractView;
import at.aau.dwaspgui.viewmodel.ViewModel;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Provides methods to show views on the main window as well as dialogs.
 * 
 * @author Philip Gasteiger
 */
public class WindowManager {
	private final Stage primaryStage;
	private File lastFileChooserLocation = null;
	private Map<ViewModel, Stage> dialogMap = new HashMap<ViewModel, Stage>();

	public WindowManager(Stage stage) {
		this.primaryStage = stage;
	}

	public void show(ViewModel viewModel) {
		AbstractView<?> view = ViewLocator.createView(viewModel);
		Scene scene = ViewLocator.loadScene(view);
		
		JFXUtil.runOnJFX(() -> {
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.toFront();
			primaryStage.setTitle(viewModel.getTitle());
			primaryStage.getIcons().clear();
			primaryStage.getIcons().add(viewModel.getIcon());
		});
	}
	
	public void showModalDialog(ViewModel viewModel) {
		AbstractView<?> view = ViewLocator.createView(viewModel);
		
		JFXUtil.runOnJFX(() -> {
			Stage dialogStage = new Stage();

			dialogMap.put(viewModel, dialogStage);
			
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setScene(ViewLocator.loadScene(view));
			dialogStage.getIcons().add(viewModel.getIcon());
			dialogStage.setTitle(viewModel.getTitle());
			dialogStage.setResizable(false);
			dialogStage.showAndWait();
		});
	}
	
	public void closeModalDialog(ViewModel viewModel) {
		Stage stage = dialogMap.get(viewModel);
		
		if (stage == null) return;
		
		stage.close();
		dialogMap.remove(viewModel);
	}
	
	public File chooseDirectory() {
		DirectoryChooser chooser = new DirectoryChooser();
		return chooser.showDialog(primaryStage);
	}
	
	public File chooseFile() {
		FileChooser chooser = new FileChooser();
		
		if (lastFileChooserLocation != null) {
			chooser.setInitialDirectory(lastFileChooserLocation);
		}
		
		File chosenFile = chooser.showOpenDialog(primaryStage);
		
		if (chosenFile != null) {
			lastFileChooserLocation = chosenFile.getParentFile();
		}
		
		return chosenFile;
	}
	
	public void showErrorDialog(Messages message, Exception details, Object... arguments) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message.format(arguments));
		alert.show();
	}
	
	public void showErrorDialog(Messages message, Object... arguments) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message.format(arguments));
		alert.show();
	}
}
