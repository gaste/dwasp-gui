package at.aau.dwaspgui.view;

import at.aau.dwaspgui.viewmodel.ViewModel;
import javafx.fxml.Initializable;

/**
 * Abstract view of the MVVM pattern.
 *  
 * @author Philip Gasteiger
 */
public abstract class AbstractView<ViewModelType extends ViewModel> implements Initializable {
	protected final ViewModelType viewModel;
	
	public AbstractView () {
		this.viewModel = null;
	}
}
