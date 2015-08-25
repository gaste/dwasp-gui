package at.aau.dwaspgui.view;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * An extension of the {@link ListView} that resizes it's height to fit the
 * height of the cells.
 * @see ListView
 * @author Philip Gasteiger
 * @param <T> This type is used to represent the type of the objects stored in
 *            the ListViews items ObservableList. It is also used in the
 *            selection model and focus model.
 */
public class ResizingListView<T> extends ListView<T> {
	/** The height of a cell */
	private final DoubleProperty cellHeight = new SimpleDoubleProperty(24.0d);

	/** @see ListView#ListView() */
	public ResizingListView() {
		super();
		initialize();
	}

	/** @see ListView#ListView(ObservableList) */
	public ResizingListView(ObservableList<T> items) {
		super(items);
		initialize();
	}
	
	private void initialize() {
		itemsProperty().addListener((change) -> {
			prefHeightProperty().bind(Bindings.size(getItems()).multiply(cellHeight));
			getItems().addListener((InvalidationListener) observable -> {
				// ensure that the height is determined after the control is layed out
				Platform.runLater(() -> determineCellHeight(this));
			});
		});
	}
	
	/**
	 * Searches for a {@link ListCell} in the scene graph and stores the height
	 * of that cell.
	 * @param node The root node of the (sub-)tree to inspect.
	 * @return <code>true</code>, if the cell height has been determined,
	 *         <code>false</code> otherwise.
	 */
	private boolean determineCellHeight(Node node) {
		if (node instanceof ListCell<?>) {
			ListCell<?> cell = (ListCell<?>) node;
			cellHeight.set(cell.getHeight());
			return true;
		}
		
		if (node instanceof Parent) {
			Parent p = (Parent) node;
			for (Node child : p.getChildrenUnmodifiable()) {
				if (determineCellHeight(child))
					return true;
			}
		}
		
		return false;
	}
}
