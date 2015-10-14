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

package at.aau.dwaspgui.app.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application preferences
 * 
 * @author Philip Gasteiger
 */
public enum ApplicationPreferences {
	COMMAND_GROUNDER("command.grounder", "gringo")
  , COMMAND_DEBUGGER("command.debugger", "dwasp")
  , FILECHOOSER_LAST_LOCATION("filechooser.lastlocation", System.getProperty("user.home"))
  ;
	
	/** logger instance */
	private static final Logger log = LoggerFactory.getLogger(ApplicationPreferences.class);
	
	/** properties file for storing the preferences */
	private static final File propertiesFile = new File(System.getProperty("user.home"), ".dwasp-gui/settings.properties");
	
	/** properties containing the preferences */
	private static final Properties props;
	
	/** key of the preferences */
	private final String key;

	/** default value of the preference */
	private final String defaultValue;
	
	/**
	 * Initialize the properties
	 */
	static {
		Properties defaultProperties = new Properties();
		
		for (ApplicationPreferences pref : ApplicationPreferences.values()) {
			defaultProperties.setProperty(pref.key, pref.defaultValue);
		}
		
		props = new Properties(defaultProperties);
		
		// check if the properties file exists, otherwise create it
		if (!propertiesFile.exists()) {
			// an empty properties file
			save();
		}
		
		load();
	}
	
	/**
	 * Load the properties from the properties file.
	 */
	private static void load() {
		try {
			props.load(new FileInputStream(propertiesFile));
		} catch (IOException e) {
			log.error("Could not load the properties from the file '{}'. Using default values.", propertiesFile.getAbsolutePath(), e);
		}
	}
	
	/**
	 * Save the properties to the properties file.
	 */
	private static void save() {
		try {
			props.store(new FileOutputStream(propertiesFile), null);
		} catch (IOException e) {
			log.error("Could not save the properties to the file '{}'.", propertiesFile.getAbsolutePath(), e);
		}
	}

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
		return props.getProperty(key);
	}

	/**
	 * Set the user preference to the given value. After this method returns,
	 * the properties are saved in an persistent storage.
	 * @param value The value of the user preference.
	 */
	public void set(String value) {
		props.setProperty(key, value);
		save();
	}
}
