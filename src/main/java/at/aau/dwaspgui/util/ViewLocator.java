package at.aau.dwaspgui.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import at.aau.dwaspgui.view.AbstractView;
import at.aau.dwaspgui.viewmodel.ViewModel;

/**
 * Locates views + scenes according to a naming convention.
 * 
 * @author Philip Gasteiger
 */
public class ViewLocator {
	private static final String FXML_FILE_ENDING = ".fxml";
	
	/**
	 * Load the scene from an .fxml file in the same package and with the same
	 * name of the view.
	 */
	public static Scene loadScene(AbstractView<?> view) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		String fxmlFilename = view.getClass().getSimpleName() + FXML_FILE_ENDING;
		String resourceBaseName = view.getClass().getCanonicalName();
		URL fxmlResource = view.getClass().getResource(fxmlFilename);

		fxmlLoader.setController(view);
		fxmlLoader.setLocation(fxmlResource);
		fxmlLoader.setResources(ResourceBundle.getBundle(resourceBaseName, Locale.ENGLISH));

		try {
			return new Scene(fxmlLoader.load());
		} catch (IOException e) {
			throw new RuntimeException("Could not load the FXML file", e);
		}
	}
	
	/**
	 * Create a new view instance for the given viewModel according to the
	 * convention <code>foo.bar.viewmodel.FooViewModel</code> gets mapped to
	 * <code>foo.bar.view.FooView</code>
	 */
	public static AbstractView<?> createView(ViewModel viewModel) {
		String viewClassName = getViewClassName(viewModel);

		try {
			AbstractView<?> viewInstance = Class.forName(viewClassName).asSubclass(AbstractView.class).newInstance();
			
			// set the viewmodel via reflection
			Field viewModelField = AbstractView.class.getDeclaredField("viewModel");
			viewModelField.setAccessible(true);
			viewModelField.set(viewInstance, viewModel);
			viewModelField.setAccessible(false);
			
			return viewInstance;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("View class '" + viewClassName + "' not found", e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Could not instantiate the view class '" + viewClassName + "'", e);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException("Could not set the 'viewModel' for the view class '" + viewClassName + "'", e);
		}
	}
	
	private static String getViewClassName(ViewModel viewModel) {
		String viewClassPackage = viewModel.getClass().getPackage().getName().replace("viewmodel", "view");
		String viewClassSimpleName = viewModel.getClass().getSimpleName().replace("ViewModel", "View");
		return viewClassPackage + "." + viewClassSimpleName;
	}
}
