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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.aau.dwaspgui.app.config.ApplicationPreferences;
import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.util.Messages;
import at.aau.dwaspgui.util.locators.FXMLLocator;
import at.aau.dwaspgui.util.locators.ViewLocator;
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
 * @author Philip Gasteiger
 */
public class WindowManager {
	private static final Logger log = LoggerFactory.getLogger(WindowManager.class);
	private final Stage primaryStage;
	private final Map<ViewModel, Stage> dialogMap = new HashMap<ViewModel, Stage>();

	public WindowManager(Stage stage) {
		this.primaryStage = stage;
	}

	public void show(ViewModel viewModel) {
		AbstractView<?> view = ViewLocator.locateForViewModel(viewModel);
		Scene scene = FXMLLocator.locateForView(view);
		
		JFXUtil.runOnJFX(() -> {
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.toFront();
			primaryStage.setTitle(viewModel.getTitle());
			primaryStage.getIcons().clear();
			primaryStage.getIcons().addAll(viewModel.getIcons());
		});
	}
	
	public void showModalDialog(ViewModel viewModel) {
		AbstractView<?> view = ViewLocator.locateForViewModel(viewModel);
		
		JFXUtil.runOnJFX(() -> {
			Stage dialogStage = new Stage();

			dialogMap.put(viewModel, dialogStage);
			
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setScene(FXMLLocator.locateForView(view));
			dialogStage.getIcons().addAll(viewModel.getIcons());
			dialogStage.setTitle(viewModel.getTitle());
			dialogStage.showAndWait();
		});
	}
	
	public void closeModalDialog(ViewModel viewModel) {
		Stage stage = dialogMap.get(viewModel);
		
		if (stage == null) {
			log.warn("Tried closing the modal dialog for view model class '{}', but did not find a stage", viewModel.getClass());
			return;
		}
		
		stage.close();
		dialogMap.remove(viewModel);
	}
	
	public File chooseDirectory() {
		DirectoryChooser chooser = new DirectoryChooser();
		return chooser.showDialog(primaryStage);
	}
	
	public File chooseDirectory(File initialDirectory) {
		DirectoryChooser chooser = new DirectoryChooser();
		
		if (null != initialDirectory && initialDirectory.exists() && initialDirectory.isDirectory())
			chooser.setInitialDirectory(initialDirectory);
		
		return chooser.showDialog(primaryStage);
	}
	
	public File chooseFile() {
		FileChooser chooser = new FileChooser();
		File initialDirectory = new File(ApplicationPreferences.FILECHOOSER_LAST_LOCATION.get());
		
		if (initialDirectory.isDirectory() && initialDirectory.exists()) {
			chooser.setInitialDirectory(initialDirectory);
		}
		
		File chosenFile = chooser.showOpenDialog(primaryStage);
		
		if (chosenFile != null) {
			ApplicationPreferences.FILECHOOSER_LAST_LOCATION.set(chosenFile.getParentFile().getAbsolutePath());
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
