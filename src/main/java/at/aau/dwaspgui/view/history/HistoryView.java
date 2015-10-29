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

import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.util.locators.FXMLLocator;
import at.aau.dwaspgui.viewmodel.history.HistoryViewModel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Control for displaying a single history item
 * @author Philip Gasteiger
 */
public class HistoryView extends HBox {
	@FXML private Label atomNameLabel;
	@FXML private FontAwesomeIconView answerIcon;
	private final HistoryViewModel viewModel;
	
	public HistoryView(HistoryViewModel viewModel) {
		FXMLLocator.locateForController(this, true);
		
		this.viewModel = viewModel;

		atomNameLabel.textProperty().bind(viewModel.atomProperty());
		
		if (QueryAnswer.YES == viewModel.getAnswer())
			answerIcon.setGlyphName(FontAwesomeIcon.CHECK.name());
		
		if (QueryAnswer.NO == viewModel.getAnswer())
			answerIcon.setGlyphName(FontAwesomeIcon.TIMES.name());
	}
	
	@FXML
	public void undo() {
		viewModel.undo();
	}
}
