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

import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.util.locators.FXMLLocator;
import at.aau.dwaspgui.viewmodel.query.QueryViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * Control for answering a single query.
 * @author Philip Gasteiger
 */
public class QueryView extends HBox {
	@FXML private Label atomNameLabel;
	@FXML private ToggleButton trueButton;
	@FXML private ToggleButton falseButton;
	@FXML private ToggleButton undefinedButton;
	
	private final ToggleGroup answerGroup = new ToggleGroup();
	private final QueryViewModel viewModel;
	
	public QueryView(QueryViewModel viewModel) {
		FXMLLocator.locateForController(this, true);
		
		this.viewModel = viewModel;

		atomNameLabel.textProperty().bind(viewModel.atomProperty());
		
		setToggleGroup();
		bindToggleButtons();
	}
	
	private void setToggleGroup() {
		trueButton.setToggleGroup(answerGroup);
		falseButton.setToggleGroup(answerGroup);
		undefinedButton.setToggleGroup(answerGroup);
	}
	
	private void bindToggleButtons() {
		// bind toggle group --> view model
		answerGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
			if (trueButton == newToggle)      viewModel.setAnswer(QueryAnswer.YES);
			if (falseButton == newToggle)     viewModel.setAnswer(QueryAnswer.NO);
			if (undefinedButton == newToggle) viewModel.setAnswer(QueryAnswer.UNKNOWN);
			if (null == newToggle) answerGroup.selectToggle(oldToggle);
		});
		
		// bind view model --> toggle group
		viewModel.answerProperty().addListener((obs, oldAnswer, newAnswer) -> {
			switch (newAnswer) {
			case YES:     answerGroup.selectToggle(trueButton);      break;
			case NO:      answerGroup.selectToggle(falseButton);     break;
			case UNKNOWN: answerGroup.selectToggle(undefinedButton); break;
			}
		});
	}
}
