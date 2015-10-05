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

package at.aau.dwaspgui.view.query;

import java.util.HashMap;
import java.util.Map;

import at.aau.dwaspgui.util.locators.FXMLLocator;
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
		FXMLLocator.locateForController(this, true);
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
