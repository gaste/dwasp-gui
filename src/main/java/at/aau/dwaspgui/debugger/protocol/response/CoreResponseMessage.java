package at.aau.dwaspgui.debugger.protocol.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.aau.Rule;
import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;

/**
 * A message from DWASP containing the debug atoms currently inside the core.
 * @author Philip Gasteiger
 */
public class CoreResponseMessage extends ResponseMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "core";
	
	/** logger instance */
	private static final Logger log = LoggerFactory.getLogger(CoreResponseMessage.class);
	
	/** list of all debug atoms in the core */
	private final List<String> debugAtoms;
	
	public CoreResponseMessage(String message) {
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIM_PART).length());
		
		if (unpacked.isEmpty())
			debugAtoms = Collections.emptyList();
		else
			debugAtoms = Arrays.asList(getMessageParts(unpacked));
	}
	
	/**
	 * Get the list of debug atoms inside the core.
	 * @return The list of debug atoms.
	 */
	List<String> getCoreDebugAtoms() {
		return debugAtoms;
	}
	
	/**
	 * Get the current items in the core
	 * @return The list of core items
	 */
	public List<CoreItem> getCoreItems(Map<String, Rule> debugRuleMap, Collection<Encoding> encodings) {
		Map<Rule, List<Map<String, String>>> rules = new HashMap<Rule, List<Map<String, String>>>();
		List<CoreItem> coreItems = new ArrayList<CoreItem>();
		
		// parse the debug atoms and generate the core items
		for (String debugAtom : debugAtoms) {
			int parenIdx = debugAtom.indexOf('(');
			
			String debugConstant = -1 != parenIdx
					? debugAtom.substring(0, parenIdx)
					: debugAtom;
			
			String term = -1 != parenIdx
					? debugAtom.substring(parenIdx + 1, debugAtom.length() - 1)
					: "";
			
			Rule coreRule = debugRuleMap.get(debugConstant);
			
			if (null == coreRule) {
				log.warn("Did not find a rule for debug constant '{}'. Debug constant to rule map: {}", debugConstant, debugRuleMap);
			} else {
				if (!rules.containsKey(coreRule)) {
					rules.put(coreRule, new ArrayList<Map<String, String>>());
				}
				
				List<Map<String, String>> substitutions = rules.get(coreRule);
				substitutions.add(coreRule.getSubstitution(term));
			}
			
			for (Rule rule : rules.keySet()) {
				List<Map<String, String>> substitutions = rules.get(rule);
				
				for (Encoding enc : encodings) {
					int ruleIdx = enc.getContent().indexOf(rule.getRule());
					
					if (-1 != ruleIdx) {
						coreItems.add(new CoreItem(enc, rule, substitutions));
					}
				}
			}
		}
		return coreItems;
	}
}
