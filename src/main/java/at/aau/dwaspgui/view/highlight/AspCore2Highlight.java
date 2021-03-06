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

package at.aau.dwaspgui.view.highlight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.StyleSpan;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;

/**
 * Syntax highlight for the ASP Core 2 language.
 * 
 * @author Philip Gasteiger
 */
public class AspCore2Highlight {
	private static final String[] KEYWORDS = new String[] {
			"\\|", "\\b(not)\\b", ":-", ":~", "#count", "#max", "#min", "#sum",
			"assertTrue", "assertFalse"
	};

	private static final String KEYWORD_PATTERN = String.join("|", KEYWORDS);
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String DOT_PATTERN = "\\.(?!\\.)";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String COMMENT_SINGLE_LINE = "%(?!\\*)[^\n]*";
	private static final String COMMENT_MULTI_LINE = "%\\*(.|\\R)*\\*%";
	private static final String COMMENT_PATTERN = COMMENT_SINGLE_LINE + "|" + COMMENT_MULTI_LINE;
	
	private static final String GROUP_KEYWORD = "KEYWORD";
	private static final String GROUP_PAREN   = "PAREN";
	private static final String GROUP_BRACE   = "BRACE";
	private static final String GROUP_BRACKET = "BRACKET";
	private static final String GROUP_DOT     = "DOT";
	private static final String GROUP_STRING  = "STRING";
	private static final String GROUP_COMMENT = "COMMENT";

	private static final String CSS_COMMENT = "comment";
	private static final String CSS_STRING = "string";
	private static final String CSS_DOT = "dot";
	private static final String CSS_BRACKET = "bracket";
	private static final String CSS_BRACE = "brace";
	private static final String CSS_PAREN = "paren";
	private static final String CSS_KEYWORD = "keyword";
	private static final String CSS_CORE_ELEMENT = "core-element";
	
	private static final Pattern HIGHLIGHT_PATTERN = Pattern.compile(
               "(?<" + GROUP_KEYWORD + ">" + KEYWORD_PATTERN + ")"
            + "|(?<" + GROUP_PAREN + ">" + PAREN_PATTERN + ")"
            + "|(?<" + GROUP_BRACE + ">" + BRACE_PATTERN + ")"
            + "|(?<" + GROUP_BRACKET + ">" + BRACKET_PATTERN + ")"
            + "|(?<" + GROUP_DOT + ">" + DOT_PATTERN + ")"
            + "|(?<" + GROUP_STRING + ">" + STRING_PATTERN + ")"
            + "|(?<" + GROUP_COMMENT + ">" + COMMENT_PATTERN + ")"
    );
	
	public static StyleSpans<Collection<String>> computeHighlighting(String text) {
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		Matcher matcher = HIGHLIGHT_PATTERN.matcher(text);
        
		int lastMatchEnd = 0;
        
        while(matcher.find()) {
            String styleClass =
                    matcher.group(GROUP_KEYWORD) != null ? CSS_KEYWORD :
                    matcher.group(GROUP_PAREN)   != null ? CSS_PAREN :
                    matcher.group(GROUP_BRACE)   != null ? CSS_BRACE :
                    matcher.group(GROUP_BRACKET) != null ? CSS_BRACKET :
                    matcher.group(GROUP_DOT)     != null ? CSS_DOT :
                    matcher.group(GROUP_STRING)  != null ? CSS_STRING :
                    matcher.group(GROUP_COMMENT) != null ? CSS_COMMENT :
                    null;
            
            spansBuilder.add(new ArrayList<String>(), matcher.start() - lastMatchEnd);
            spansBuilder.add(new ArrayList<String>(Arrays.asList(styleClass)), matcher.end() - matcher.start());
            lastMatchEnd = matcher.end();
        }
        
        spansBuilder.add(new ArrayList<String>(), text.length() - lastMatchEnd);
        return spansBuilder.create();
	}

	public static StyleSpans<Collection<String>> computeHighlighting(Encoding encoding, String text) {
		return computeHighlighting(encoding, text, Collections.emptyList());
	}
	
	public static StyleSpans<Collection<String>> computeHighlighting(Encoding encoding, String text, List<CoreItem> coreItems) {
		// compute the basic highlighting
		StyleSpans<Collection<String>> styleSpans = computeHighlighting(text);
        
		// add the highlighting for the core items
        for (CoreItem ci : coreItems) {
			if (ci.getEncoding().equals(encoding)) {
				StyleSpans<Collection<String>> sv = styleSpans.subView(ci.getFromIndex(), ci.getFromIndex() + ci.getLength());
				
				for (StyleSpan<Collection<String>> s : sv) {
					s.getStyle().add(CSS_CORE_ELEMENT);
				}
				
				styleSpans.concat(sv);
			}
        }
        
        return styleSpans;
	}
}
