package at.aau.dwaspgui.debug;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javafx.beans.property.BooleanProperty;
import javafx.util.Pair;
import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;


public interface Debugger {
	void startDebugger(Set<Encoding> program, TestCase testCase) throws DebuggerException;
	void stopDebugger();
	void assertAtoms(List<Pair<String, QueryAnswer>> assertions);
	void registerCoreCallback(Consumer<List<CoreItem>> callback);
	void registerQueryCallback(Consumer<List<String>> callback);
	BooleanProperty isRunning();
	BooleanProperty isComputingCore();
	BooleanProperty isComputingQuery();
}
