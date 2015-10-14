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

package at.aau.dwaspgui.view;

import java.net.URL;
import java.util.ResourceBundle;

import at.aau.dwaspgui.viewmodel.NewProjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NewProjectView extends AbstractView<NewProjectViewModel> {
	@FXML TextField nameTextField;
	@FXML TextField locationTextField;
	@FXML Button createButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameTextField.textProperty().bindBidirectional(viewModel.projectNameProperty());
		locationTextField.textProperty().bindBidirectional(viewModel.locationProperty());
		createButton.disableProperty().bind(viewModel.createDisabledProperty());
	}
	
	@FXML public void chooseLocationAction() { viewModel.chooseLocation(); }
	@FXML public void cancelAction() { viewModel.cancel(); }
	@FXML public void createAction() { viewModel.create(); }
}
