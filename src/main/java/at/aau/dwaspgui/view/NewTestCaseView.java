package at.aau.dwaspgui.view;

import java.net.URL;
import java.util.ResourceBundle;

import at.aau.dwaspgui.viewmodel.NewTestCaseViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NewTestCaseView extends AbstractView<NewTestCaseViewModel> {
	@FXML TextField nameTextField;
	@FXML Button createButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameTextField.textProperty().bindBidirectional(viewModel.testCaseNameProperty());
		createButton.disableProperty().bind(viewModel.createDisabledProperty());
	}
	
	@FXML public void cancelAction() { viewModel.cancel(); }
	@FXML public void createAction() { viewModel.create(); }
}
