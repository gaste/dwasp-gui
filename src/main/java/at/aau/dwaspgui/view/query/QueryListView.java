package at.aau.dwaspgui.view.query;

import java.util.HashMap;
import java.util.Map;

import at.aau.dwaspgui.util.ViewLocator;
import at.aau.dwaspgui.viewmodel.query.QueryViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

/**
 * Control for displaying a list of queries.
 * @author Philip Gasteiger
 */
public class QueryListView extends VBox {
	private final ObservableList<QueryViewModel> queries;
	private final Map<QueryViewModel, QueryView> queryMap = new HashMap<QueryViewModel, QueryView>();
	
	public QueryListView() {
		ViewLocator.loadControlView(this);
		queries = FXCollections.observableArrayList();
		queries.addListener((ListChangeListener<QueryViewModel>) c -> {
			while(c.next()) {
				for (QueryViewModel removed : c.getRemoved()) {
					getChildren().remove(queryMap.get(removed));
					queryMap.remove(removed);
				}
				
				for (QueryViewModel added : c.getAddedSubList()) {
					QueryView view = new QueryView(added);
					getChildren().add(view);
					queryMap.put(added, view);
				}
			}
		});
	}
	
	public ObservableList<QueryViewModel> getQueries() { return queries; }
}
