package at.aau.dwaspgui.debugger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;
import javafx.beans.property.BooleanProperty;

/**
 * Debugger for ASP programs.
 * @author Philip Gasteiger
 */
public interface Debugger {
	/**
	 * Start the debugger with the given <code>program</program> and
	 * <code>testCase</code>.
	 * @param program  The program to debug.
	 * @param testCase The test case.
	 * @throws DebuggerException If the debugger could not be started.
	 */
	void startDebugger(Collection<Encoding> program, TestCase testCase) throws DebuggerException;
	
	/**
	 * Stop the debugger, if it is currently running.
	 */
	void stopDebugger();
	
	/**
	 * Assert the truth values of the given atoms.
	 * @param assertions Truth-assignment of a list of atoms.
	 */
	void assertAtoms(Map<String, QueryAnswer> assertions);
	
	/**
	 * Register a handler that gets called when the items inside the UNSAT core
	 * change.
	 * @param callback The handler that gets called.
	 */
	void registerCoreCallback(Consumer<List<CoreItem>> callback);
	
	/**
	 * Register a handler that gets called when the query atoms change.
	 * @param callback The handler that gets called.
	 */
	void registerQueryCallback(Consumer<List<String>> callback);
	
	/** 
	 * @return A property indicating whether the debugger is running.
	 */
	BooleanProperty isRunning();
	
	/**
	 * @return A property indicating whether the debugger is computing the UNSAT
	 *         core.
	 */
	BooleanProperty isComputingCore();
	
	/**
	 * @return A property indicating whether the debugger is computing a query.
	 */
	BooleanProperty isComputingQuery();
}
