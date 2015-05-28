package at.aau.util;

import javafx.application.Platform;

/**
 * Utility class for JFX
 */
public class JFXUtil {
	/**
	 * Runs the given runnable on the Java FX GUI thread.
	 */
	public static void runOnJFX(Runnable runnable) {
		if (Platform.isFxApplicationThread()) {
			runnable.run();
		} else {
			Platform.runLater(runnable);
		}
	}
}
