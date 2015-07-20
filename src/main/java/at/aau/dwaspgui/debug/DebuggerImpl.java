package at.aau.dwaspgui.debug;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import at.aau.GringoWrapper;
import at.aau.Rule;
import at.aau.dwaspgui.debug.protocol.CoreResponse;
import at.aau.dwaspgui.debug.protocol.Message;
import at.aau.dwaspgui.debug.protocol.MessageParsingException;
import at.aau.dwaspgui.debug.protocol.RequestMessage;
import at.aau.dwaspgui.debug.protocol.RequestMessage.RequestType;
import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.util.Messages;
import at.aau.grounder.GroundingException;
import at.aau.postprocessing.PostprocessingException;

public class DebuggerImpl implements Debugger {
	private List<Consumer<List<CoreItem>>> coreCallbacks = new ArrayList<Consumer<List<CoreItem>>>();
	private BooleanProperty isRunning = new SimpleBooleanProperty(false);
	private BooleanProperty isComputingCore = new SimpleBooleanProperty(false);
	private BooleanProperty isComputingQuery = new SimpleBooleanProperty(false);
	private Set<Encoding> currentProgram = null;
	private Process debugger = null;
	private Map<String, Rule> debugRuleMap = null;
	
	private static final String DEBUGGER_COMMAND = "dwasp";
	private static final String DEBUGGER_OPTION_INPUT_FILE = "--debug=";
	private static final String DEBUG_FILE_NAME = "debug.dbg";
	private static final String GRINGO_WRAPPER_GROUNDER = "gringo";
	private static final String GRINGO_WRAPPER_OPTIONS = "";
	private static final String GRINGO_WRAPPER_DEBUGCONSTANT = "_debug";
	
	@Override
	public void startDebugger(Set<Encoding> program, TestCase testCase)
			throws DebuggerException{
		isRunning.set(true);
		
		currentProgram = program;
		
		groundProgram(program, DEBUG_FILE_NAME);
		startDebugger(DEBUG_FILE_NAME);
	}
	
	private void groundProgram(Set<Encoding> program, String filename)
			throws DebuggerException {
		StringBuilder inputProgram = new StringBuilder();
		
		for (Encoding encoding : program) {
			inputProgram.append(encoding.getContent());
		}

		GringoWrapper wrapper = new GringoWrapper(GRINGO_WRAPPER_GROUNDER,
				GRINGO_WRAPPER_OPTIONS, GRINGO_WRAPPER_DEBUGCONSTANT, false, false);
		
		try {
			debugRuleMap = new HashMap<String, Rule>();
			String groundedProgram = wrapper.ground(inputProgram.toString(), true, debugRuleMap);
			
			Files.write(Paths.get(filename), groundedProgram.getBytes());
		} catch (GroundingException | PostprocessingException e) {
			throw new DebuggerException(Messages.ERROR_GROUNDING.format(), e);
		} catch (IOException e) {
			throw new DebuggerException(Messages.ERROR_GROUNDING.format(), e);
		}
	}
	
	private void startDebugger(String filename) throws DebuggerException {
		ProcessBuilder builder = new ProcessBuilder(DEBUGGER_COMMAND, DEBUGGER_OPTION_INPUT_FILE + filename);
		
		try {
			debugger = builder.start();
			notifyCores();
		} catch (IOException e) {
			throw new DebuggerException(Messages.ERROR_START_DEBUGGER.format(), e);
		}
	}
	
	private Runnable coreRequest = () -> {
		try {
			RequestMessage request = new RequestMessage(RequestType.GET_CORE);
			request.writeToOutputStream(debugger.getOutputStream());
			
			Message resp = Message.parseFromInputStream(debugger.getInputStream());
			
			if (resp instanceof CoreResponse) {
				CoreResponse response = (CoreResponse) resp;
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
					
					if (!rules.containsKey(rule)) {
						rules.put(rule, new ArrayList<Map<String,String>>());
					}
					
					List<Map<String, String>> substitutions = rules.get(rule);
					substitutions.add(rule.getSubstitution(term));
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
			e.printStackTrace();
		} catch (MessageParsingException e) {
			e.printStackTrace();
		}
	};
	
	private void notifyCores() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(coreRequest);
		executor.shutdown();
	}

	@Override
	public void stopDebugger() {
		isRunning.set(false);
		currentProgram = null;
		debugRuleMap = null;
		
		if (debugger != null && debugger.isAlive()) 
			debugger.destroy();
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
