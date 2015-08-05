package at.aau.dwaspgui.debug;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;
import javafx.beans.property.BooleanProperty;


public interface Debugger {
	void startDebugger(Collection<Encoding> program, TestCase testCase) throws DebuggerException;
	void stopDebugger();
	void assertAtoms(Map<String, QueryAnswer> assertions);
	void registerCoreCallback(Consumer<List<CoreItem>> callback);
	void registerQueryCallback(Consumer<List<String>> callback);
	BooleanProperty isRunning();
	BooleanProperty isComputingCore();
	BooleanProperty isComputingQuery();
}
