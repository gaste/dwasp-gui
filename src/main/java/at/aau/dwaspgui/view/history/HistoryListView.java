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

package at.aau.dwaspgui.view.history;

import java.util.HashMap;
import java.util.Map;

import at.aau.dwaspgui.util.locators.FXMLLocator;
import at.aau.dwaspgui.viewmodel.history.HistoryViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

/**
 * Control for displaying a list of history items.
 * @author Philip Gasteiger
 */
public class HistoryListView extends VBox {
	private final ObservableList<HistoryViewModel> history;
	private final Map<HistoryViewModel, HistoryView> historyMap = new HashMap<HistoryViewModel, HistoryView>();
	
	public HistoryListView() {
		FXMLLocator.locateForController(this, true);
		history = FXCollections.observableArrayList();
		history.addListener((ListChangeListener<HistoryViewModel>) c -> {
			while(c.next()) {
				for (HistoryViewModel removed : c.getRemoved()) {
					getChildren().remove(historyMap.get(removed));
					historyMap.remove(removed);
				}
				
				for (HistoryViewModel added : c.getAddedSubList()) {
					HistoryView view = new HistoryView(added);
					getChildren().add(view);
					historyMap.put(added, view);
				}
			}
		});
	}
	
	public ObservableList<HistoryViewModel> getHistory() { return history; }

}
