package at.aau.dwaspgui.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;

public class DebuggerImpl implements Debugger {
	private List<Consumer<List<CoreItem>>> coreCallbacks = new ArrayList<Consumer<List<CoreItem>>>();
	private BooleanProperty isRunning = new SimpleBooleanProperty(false);
	private BooleanProperty isComputingCore = new SimpleBooleanProperty(false);
	private BooleanProperty isComputingQuery = new SimpleBooleanProperty(false);
	
	@Override
	public void startDebugger(Set<Encoding> program, Encoding instance,
			TestCase testCase) {
		isRunning.set(true);
		for (Consumer<List<CoreItem>> callback : coreCallbacks) {
			CoreItem i = new CoreItem(program.iterator().next(), 2, 0);
			callback.accept(Arrays.asList(i));
		}
	}

	@Override
	public void stopDebugger() {
		isRunning.set(false);
	}

	@Override
	public void registerCoreCallback(Consumer<List<CoreItem>> callback) {
		coreCallbacks.add(callback);
	}

	@Override
	public void computeQuery(Function<String, QueryAnswer> callback) {
		QueryAnswer a = callback.apply("col(1,r)");
		System.out.println("Answered: " + a);
	}

	@Override
	public BooleanProperty isRunning() {
		return isRunning;
	}

	@Override
	public BooleanProperty isComputingCore() {
		return isComputingCore;
	}

	@Override
	public BooleanProperty isComputingQuery() {
		return isComputingQuery;
	}
}
