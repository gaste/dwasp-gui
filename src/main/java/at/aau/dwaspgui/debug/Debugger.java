package at.aau.dwaspgui.debug;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;


public interface Debugger {
	void startDebugger(Set<Encoding> program, TestCase testCase);
	void stopDebugger();
	void computeQuery(Function<String, QueryAnswer> callback);
	void registerCoreCallback(Consumer<List<CoreItem>> callback);
	BooleanProperty isRunning();
	BooleanProperty isComputingCore();
	BooleanProperty isComputingQuery();
}
