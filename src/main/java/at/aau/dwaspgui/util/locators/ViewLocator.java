package at.aau.dwaspgui.util.locators;

import java.lang.reflect.Field;

import at.aau.dwaspgui.view.AbstractView;
import at.aau.dwaspgui.viewmodel.ViewModel;

/**
 * Locates views according to a naming convention.
 * 
 * @author Philip Gasteiger
 */
public class ViewLocator {
	/**
	 * Create a new view instance for the given viewModel according to the
	 * convention <code>foo.bar.viewmodel.FooViewModel</code> gets mapped to
	 * <code>foo.bar.view.FooView</code>
	 */
	public static AbstractView<?> locateForViewModel(ViewModel viewModel) {
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
