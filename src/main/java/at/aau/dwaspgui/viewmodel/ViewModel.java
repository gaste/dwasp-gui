package at.aau.dwaspgui.viewmodel;

import java.util.List;

import javafx.scene.image.Image;

/**
 * Marker interface for a view model.
 * 
 * @author Philip Gasteiger
 */
public interface ViewModel {
	public String getTitle();
	public List<Image> getIcons();
}
