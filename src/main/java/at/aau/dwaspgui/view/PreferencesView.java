package at.aau.dwaspgui.view;

import java.net.URL;
import java.util.ResourceBundle;

import at.aau.dwaspgui.viewmodel.PreferencesViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PreferencesView extends AbstractView<PreferencesViewModel> {
	@FXML private TextField debuggerCommandTextField;
	@FXML private TextField grounderCommandTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		debuggerCommandTextField.textProperty().bindBidirectional(viewModel.debuggerCommand());
		grounderCommandTextField.textProperty().bindBidirectional(viewModel.grounderCommand());
	}
	
	@FXML public void saveAction() { viewModel.save(); }
	@FXML public void cancelAction() { viewModel.cancel(); }
	@FXML public void chooseDebuggerCommandAction() { viewModel.chooseDebuggerCommand(); }
	@FXML public void chooseGrounderCommandAction() { viewModel.chooseGrounderCommand(); }
}
