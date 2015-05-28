package at.aau.view;

import at.aau.viewmodel.ViewModel;

/**
 * Abstract view of the MVVM pattern.
 *  
 * @author Philip Gasteiger
 */
public class AbstractView<ViewModelType extends ViewModel> {
	protected final ViewModelType viewModel;
	
	public AbstractView () {
		this.viewModel = null;
	}
}
