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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.aau.GringoWrapper;
import at.aau.Rule;
import at.aau.dwaspgui.app.config.ApplicationPreferences;
import at.aau.dwaspgui.debugger.protocol.Message;
import at.aau.dwaspgui.debugger.protocol.MessageParsingException;
import at.aau.dwaspgui.debugger.protocol.ReadableMessage;
import at.aau.dwaspgui.debugger.protocol.assertion.AssertionMessage;
import at.aau.dwaspgui.debugger.protocol.assertion.UndoAssertionMessage;
import at.aau.dwaspgui.debugger.protocol.info.ComputingCoreInfoMessage;
import at.aau.dwaspgui.debugger.protocol.info.ComputingQueryInfoMessage;
import at.aau.dwaspgui.debugger.protocol.info.ProgramCoherentInfoMessage;
import at.aau.dwaspgui.debugger.protocol.info.UnfoundedCoreInfoMessage;
import at.aau.dwaspgui.debugger.protocol.request.RequestMessage;
import at.aau.dwaspgui.debugger.protocol.request.RequestMessage.RequestType;
import at.aau.dwaspgui.debugger.protocol.response.CoreResponseMessage;
import at.aau.dwaspgui.debugger.protocol.response.QueryResponseMessage;
import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.util.Messages;
import at.aau.grounder.GroundingException;
import at.aau.postprocessing.PostprocessingException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * DWASP-{@link Debugger} implementation.
 * @author Philip Gasteiger
 */
public class DebuggerImpl implements Debugger {
	private static final Logger log = LoggerFactory.getLogger(DebuggerImpl.class);
	
	private List<Consumer<List<CoreItem>>> coreCallbacks = new ArrayList<Consumer<List<CoreItem>>>();
	private List<Consumer<List<String>>> queryCallbacks = new ArrayList<Consumer<List<String>>>();
	private List<Consumer<List<String>>> coherentCallbacks = new ArrayList<Consumer<List<String>>>();
	private List<Runnable> computeCoreCallbacks = new ArrayList<Runnable>();
	private List<Runnable> computeQueryCallbacks = new ArrayList<Runnable>();
	
	private BooleanProperty isRunning = new SimpleBooleanProperty(false);
	private BooleanProperty isComputingCore = new SimpleBooleanProperty(false);
	private BooleanProperty isComputingQuery = new SimpleBooleanProperty(false);
	private BooleanProperty isUnfoundedCase = new SimpleBooleanProperty(false);
	
	private Process debugger = null;
	private Collection<Encoding> currentProgram = null;
	private Map<String, Rule> debugRuleMap = null;
	private ExecutorService messageReaderExecutor = null;
	private ExecutorService debuggerExecutor = null;
	
	private static final String DEBUGGER_OPTION_GUI = "--debug-gui";
	private static final String DEBUGGER_OPTION_INPUT_FILE = "--debug=";
	private static final String GRINGO_WRAPPER_OPTIONS = "";
	private static final String GRINGO_WRAPPER_DEBUGCONSTANT = "_debug";
	
	@Override
	public void startDebugger(Collection<Encoding> program, TestCase testCase)
			throws DebuggerException {
		// guard: do not start the debugger twice
		if (isRunning.get()) return;
		
		isRunning.set(true);
		isUnfoundedCase.set(false);
		
		currentProgram = program;
		debuggerExecutor = Executors.newSingleThreadExecutor();
		messageReaderExecutor = Executors.newSingleThreadExecutor();
		
		try {
			File debugFile = File.createTempFile(".dbg", "debug");
			debugFile.deleteOnExit();
			
			groundProgram(program, testCase, debugFile.getAbsolutePath());
			startDebugger(debugFile.getAbsolutePath());
		} catch (IOException e) {
			log.error("Could not create a temporary file for the debugger", e);
			throw new DebuggerException(Messages.ERROR_GROUNDING.format());
		}
	}
	
	private void groundProgram(Collection<Encoding> program, TestCase testCase, String filename)
			throws DebuggerException {
		StringBuilder inputProgram = new StringBuilder();
		
		for (Encoding encoding : program) {
			inputProgram.append(encoding.getContent());
		}
		
		inputProgram.append(testCase.getAssertions());

		GringoWrapper wrapper = new GringoWrapper(ApplicationPreferences.COMMAND_GROUNDER.get(),
				GRINGO_WRAPPER_OPTIONS, GRINGO_WRAPPER_DEBUGCONSTANT, false, false);
		
		try {
			debugRuleMap = new HashMap<String, Rule>();
			String groundedProgram = wrapper.ground(inputProgram.toString(), true, debugRuleMap);
			
			Files.write(Paths.get(filename), groundedProgram.getBytes());
		} catch (GroundingException | PostprocessingException | IOException e) {
			log.error("Could not ground the logic program.", e);
			throw new DebuggerException(Messages.ERROR_GROUNDING.format(), e);
		}
	}
	
	private void startDebugger(String filename) throws DebuggerException {
		ProcessBuilder builder = new ProcessBuilder(ApplicationPreferences.COMMAND_DEBUGGER.get(), DEBUGGER_OPTION_GUI, DEBUGGER_OPTION_INPUT_FILE + filename);
		
		try {
			debugger = builder.start();
			
			messageReaderExecutor.execute(messageReader);
			
			notifyCores();
			getQuery();
		} catch (IOException e) {
			log.error("Could not start the debugger.", e);
			throw new DebuggerException(Messages.ERROR_START_DEBUGGER.format(), e);
		}
	}
	
	private final Runnable messageReader = () -> {
		InputStreamReader reader = new InputStreamReader(debugger.getInputStream(), Message.CHARSET);
		while(isRunning.get()) {
			try{
				ReadableMessage msg = Message.parseFromInputStream(reader);
				
				if (msg instanceof CoreResponseMessage) {
					CoreResponseMessage response = (CoreResponseMessage) msg;
					
					coreCallbacks.forEach(c -> c.accept(response.getCoreItems(debugRuleMap, currentProgram)));
				} else if (msg instanceof QueryResponseMessage) {
					QueryResponseMessage response = (QueryResponseMessage) msg;
					
					queryCallbacks.forEach(c -> c.accept(response.getAtoms().subList(0, response.getAtoms().size() > 9 ? 9 : response.getAtoms().size())));
				} else if (msg instanceof ProgramCoherentInfoMessage) {
					ProgramCoherentInfoMessage info = (ProgramCoherentInfoMessage) msg;
					
					coherentCallbacks.forEach(c -> c.accept(info.getAnswerSet()));
					stopDebugger();
				} else if (msg instanceof ComputingCoreInfoMessage) {
					computeCoreCallbacks.forEach(c -> c.run());
				} else if (msg instanceof ComputingQueryInfoMessage) {
					computeQueryCallbacks.forEach(c -> c.run());
				} else if (msg instanceof UnfoundedCoreInfoMessage) {
					isUnfoundedCase.set(true);
				}
			} catch (MessageParsingException e) {
				log.error("Could not parse the core response from the debugger.", e);
			}
		}
	};
	
	private Runnable coreRequest = () -> {
		try {
			RequestMessage request = new RequestMessage(RequestType.GET_CORE);
			request.writeToOutputStream(debugger.getOutputStream());
		} catch (IOException e) {
			log.error("Could not write the core request to the debugger.", e);
		}
	};
	
	private Runnable queryRequest = () -> {
		try{
			RequestMessage request = new RequestMessage(RequestType.GET_QUERY);
			request.writeToOutputStream(debugger.getOutputStream());
		} catch (IOException e) {
			log.error("Could not write the query request to the debugger.", e);
		}
	};
	

	private void notifyCores() {
		debuggerExecutor.execute(coreRequest);
	}
	
	private void getQuery() {
		debuggerExecutor.execute(queryRequest);
	}
	
	@Override
	public void assertAtoms(Map<String, QueryAnswer> assertions) {
		debuggerExecutor.execute(() -> {
			try {
				AssertionMessage msg = new AssertionMessage(assertions);
				msg.writeToOutputStream(debugger.getOutputStream());
				
				if (!isUnfoundedCase.get()) {
					notifyCores();
					getQuery();
				}
			} catch (Exception e) {
				log.error("Could not write the assertion request to the debugger");
			}
		});
	}

	@Override
	public void undoAssertion(String atom) {
		debuggerExecutor.execute(() -> {
			try {
				UndoAssertionMessage msg = new UndoAssertionMessage(atom);
				msg.writeToOutputStream(debugger.getOutputStream());
				
				notifyCores();
				getQuery();
			} catch (Exception e) {
				log.error("Could not write the undo assertion request to the debugger");
			}
		});
	}
	
	@Override
	public void stopDebugger() {
		// guard: debugger must be running
		if (!isRunning.get()) return;
		
		isRunning.set(false);
		isUnfoundedCase.set(false);
		currentProgram = null;
		debugRuleMap = null;
		
		if (debuggerExecutor != null) {
			debuggerExecutor.shutdownNow();
			debuggerExecutor = null;
		}
		
		if (messageReaderExecutor != null) {
			messageReaderExecutor.shutdownNow();
			messageReaderExecutor = null;
		}
		
		coreCallbacks.forEach(c -> c.accept(Collections.emptyList()));
		queryCallbacks.forEach(c -> c.accept(Collections.emptyList()));
		
		if (debugger != null && debugger.isAlive()) 
			debugger.destroy();
	}

	@Override
	public void registerCoreCallback(Consumer<List<CoreItem>> callback) {
		coreCallbacks.add(callback);
	}

	@Override
	public void registerQueryCallback(Consumer<List<String>> callback) {
		queryCallbacks.add(callback);
	}
	
	@Override
	public void registerCoherentCallback(Consumer<List<String>> callback) {
		coherentCallbacks.add(callback);
	}
	
	@Override
	public void registerComputeCoreCallback(Runnable callback) {
		computeCoreCallbacks.add(callback);
	}
	
	@Override
	public void registerComputeQueryCallback(Runnable callback) {
		computeQueryCallbacks.add(callback);
	}

	@Override public BooleanProperty isRunning() { return isRunning; }
	@Override public BooleanProperty isComputingCore() { return isComputingCore; }
	@Override public BooleanProperty isComputingQuery() { return isComputingQuery; }
}
