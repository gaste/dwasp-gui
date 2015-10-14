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
	 * Register a handler that gets called when the program is coherent.
	 * @param callback The handler that gets called.
	 */
	void registerCoherentCallback(Consumer<List<String>> callback);

	/**
	 * Register a handler that gets called when the debugger is computing the
	 * core.
	 * @param callback The handler that gets called.
	 */
	void registerComputeCoreCallback(Runnable callback);
	
	/**
	 * Register a handler that gets called when the debugger is computing the
	 * query.
	 * @param callback The handler that gets called.
	 */
	void registerComputeQueryCallback(Runnable callback);
	
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
