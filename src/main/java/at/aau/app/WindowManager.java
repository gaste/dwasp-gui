package at.aau.app;

import javafx.scene.Scene;
import javafx.stage.Stage;
import at.aau.util.JFXUtil;
import at.aau.util.ViewLocator;
import at.aau.view.AbstractView;
import at.aau.viewmodel.ViewModel;

/**
 * Provides methods to show views on the main window as well as dialogs.
 * 
 * @author Philip Gasteiger
 */
public class WindowManager {
	private final Stage stage;

	public WindowManager(Stage stage) {
		this.stage = stage;
	}

	private void showScene(final Scene scene) {
		JFXUtil.runOnJFX(() -> {
			stage.setScene(scene);
			stage.show();
		});
	}

	public void show(ViewModel viewModel) {
		AbstractView<?> view = ViewLocator.createView(viewModel);
		showScene(ViewLocator.loadScene(view));
	}
}
