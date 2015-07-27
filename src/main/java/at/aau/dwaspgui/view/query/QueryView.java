package at.aau.dwaspgui.view.query;

import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.util.ViewLocator;
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
		ViewLocator.loadControlView(this);
		
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
