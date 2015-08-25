package at.aau.dwaspgui.debugger;

import java.io.IOException;
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
import at.aau.dwaspgui.debugger.protocol.assertion.AssertionMessage;
import at.aau.dwaspgui.debugger.protocol.request.RequestMessage;
import at.aau.dwaspgui.debugger.protocol.request.RequestMessage.RequestType;
import at.aau.dwaspgui.debugger.protocol.response.CoreResponseMessage;
import at.aau.dwaspgui.debugger.protocol.response.QueryResponseMessage;
import at.aau.dwaspgui.debugger.protocol.response.ResponseMessage;
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
	
	private BooleanProperty isRunning = new SimpleBooleanProperty(false);
	private BooleanProperty isComputingCore = new SimpleBooleanProperty(false);
	private BooleanProperty isComputingQuery = new SimpleBooleanProperty(false);
	
	private Process debugger = null;
	private Collection<Encoding> currentProgram = null;
	private Map<String, Rule> debugRuleMap = null;
	private ExecutorService debuggerExecutor = null;
	
	private static final String DEBUGGER_OPTION_INPUT_FILE = "--debug=";
	private static final String DEBUG_FILE_NAME = "debug.dbg";
	private static final String GRINGO_WRAPPER_OPTIONS = "";
	private static final String GRINGO_WRAPPER_DEBUGCONSTANT = "_debug";
	
	@Override
	public void startDebugger(Collection<Encoding> program, TestCase testCase)
			throws DebuggerException {
		// guard: do not start the debugger twice
		if (isRunning.get()) return;
		
		isRunning.set(true);
		
		currentProgram = program;
		debuggerExecutor = Executors.newSingleThreadExecutor();
		
		groundProgram(program, DEBUG_FILE_NAME);
		startDebugger(DEBUG_FILE_NAME);
	}
	
	private void groundProgram(Collection<Encoding> program, String filename)
			throws DebuggerException {
		StringBuilder inputProgram = new StringBuilder();
		
		for (Encoding encoding : program) {
			inputProgram.append(encoding.getContent());
		}

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
		ProcessBuilder builder = new ProcessBuilder(ApplicationPreferences.COMMAND_DEBUGGER.get(), DEBUGGER_OPTION_INPUT_FILE + filename);
		
		try {
			debugger = builder.start();
			
			notifyCores();
			getQuery();
		} catch (IOException e) {
			log.error("Could not start the debugger.", e);
			throw new DebuggerException(Messages.ERROR_START_DEBUGGER.format(), e);
		}
	}
	
	private Runnable coreRequest = () -> {
		try {
			RequestMessage request = new RequestMessage(RequestType.GET_CORE);
			request.writeToOutputStream(debugger.getOutputStream());
			
			ResponseMessage resp = Message.parseFromInputStream(debugger.getInputStream());
			
			if (resp instanceof CoreResponseMessage) {
				CoreResponseMessage response = (CoreResponseMessage) resp;
				Map<Rule, List<Map<String, String>>> rules = new HashMap<Rule, List<Map<String, String>>>();
				
				for (String coreItem : response.getCoreItems()) {
					int termIdx = coreItem.indexOf('(');

					String debugConstant = termIdx != -1 
							? coreItem.substring(0, termIdx)
							: coreItem;
					
					String term = termIdx != -1
							? coreItem.substring(termIdx + 1, coreItem.length() - 1)
							: "";
					
					Rule rule = debugRuleMap.get(debugConstant);
					if (rule != null) {
						if (!rules.containsKey(rule)) {
							rules.put(rule, new ArrayList<Map<String,String>>());
						}
						
						List<Map<String, String>> substitutions = rules.get(rule);
						substitutions.add(rule.getSubstitution(term));
					}
				}
				
				List<CoreItem> coreItems = new ArrayList<CoreItem>();
				
				rules.forEach((rule, substitutions) -> {
					for (Encoding enc : currentProgram) {
						int idx = enc.getContent().indexOf(rule.getRule());
						
						if (idx != -1) {
							coreItems.add(new CoreItem(enc, rule, substitutions));
						}
					}
				});
				
				coreCallbacks.forEach((c) -> { c.accept(coreItems); });
			}
		} catch (IOException e) {
			log.error("Could not write the core request to the debugger.", e);
		} catch (MessageParsingException e) {
			log.error("Could not parse the core response from the debugger.", e);
		}
	};
	
	private Runnable queryRequest = () -> {
		try{
			RequestMessage request = new RequestMessage(RequestType.GET_QUERY);
			request.writeToOutputStream(debugger.getOutputStream());
			
			ResponseMessage resp = Message.parseFromInputStream(debugger.getInputStream());
			
			if (resp instanceof QueryResponseMessage) {
				QueryResponseMessage response = (QueryResponseMessage) resp;
				
				queryCallbacks.forEach(c -> c.accept(response.getAtoms()));
			}
		} catch (IOException e) {
			log.error("Could not write the query request to the debugger.", e);
		} catch (MessageParsingException e) {
			log.error("Could not parse the query response from the debugger.", e);
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
				
				notifyCores();
				getQuery();
			} catch (Exception e) {
				log.error("Could not write the assertion request to the debugger");
			}
		});
	}
	
	@Override
	public void stopDebugger() {
		// guard: debugger must be running
		if (!isRunning.get()) return;
		
		isRunning.set(false);
		currentProgram = null;
		debugRuleMap = null;
		
		if (debuggerExecutor != null) {
			debuggerExecutor.shutdownNow();
			debuggerExecutor = null;
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

	@Override public BooleanProperty isRunning() { return isRunning; }
	@Override public BooleanProperty isComputingCore() { return isComputingCore; }
	@Override public BooleanProperty isComputingQuery() { return isComputingQuery; }
}
