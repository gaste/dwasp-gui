package at.aau.dwaspgui.app.config;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Application preferences
 * 
 * @author Philip Gasteiger
 */
public enum ApplicationPreferences {
	COMMAND_GROUNDER("GROUNDER", "gringo")
  , COMMAND_DEBUGGER("DEBUGGER", "dwasp")
  , FILECHOOSER_LAST_LOCATION("FILECHOOSER_LAST_LOCATION", System.getProperty("user.home"))
  ;

	/** java preferences instance for this file */
	private static final Preferences prefs = Preferences.userNodeForPackage(ApplicationPreferences.class);

	/** key of the preferences */
	private final String key;

	/** default value of the preference */
	private final String defaultValue;

	private ApplicationPreferences(String key) {
		this(key, "");
	}

	private ApplicationPreferences(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Get the value of the property.
	 * @return The user preference.
	 */
	public String get() {
		return prefs.get(key, defaultValue);
	}

	/**
	 * Set the user preference to the given value.
	 * @param value The value of the user preference.
	 */
	public void set(String value) {
		prefs.put(key, value);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
