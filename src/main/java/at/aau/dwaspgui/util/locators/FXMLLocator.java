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

package at.aau.dwaspgui.util.locators;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import at.aau.dwaspgui.view.AbstractView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Locates FXML files according to a naming convention.
 * @author Philip Gasteiger
 */
public class FXMLLocator {
	private static final String FXML_FILE_ENDING = ".fxml";

	/**
	 * Load the view from an .fxml file in the same package and with the same
	 * name of the controller.
	 * @param controller The controller of the loaded view.
	 * @param isRoot <code>true</code>, if <code>control</code> is the root of
	 * the view, <code>false</code> otherwise.
	 * @throws RuntimeException If the .fxml file could not be loaded.
	 */
	public static Parent locateForController(Object controller, boolean isRoot) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		String fxmlFilename = controller.getClass().getSimpleName() + FXML_FILE_ENDING;
		String resourceBaseName = controller.getClass().getCanonicalName();
		URL fxmlResource = controller.getClass().getResource(fxmlFilename);
	
		fxmlLoader.setLocation(fxmlResource);
		fxmlLoader.setController(controller);
		fxmlLoader.setResources(ResourceBundle.getBundle(resourceBaseName, Locale.ENGLISH));
	
		if (isRoot)
			fxmlLoader.setRoot(controller);
	
	    try {
	        return fxmlLoader.load();
	    } catch (IOException exception) {
	        throw new RuntimeException(exception);
	    }
	}

	/**
	 * Load the scene from an .fxml file in the same package and with the same
	 * name of the view.
	 * @param view The view for which the .fxml file should be loaded.
	 * @see FXMLLocator#locateForController(Object, boolean)
	 */
	public static Scene locateForView(AbstractView<?> view) {
		return new Scene(locateForController(view, false));
	}
}
