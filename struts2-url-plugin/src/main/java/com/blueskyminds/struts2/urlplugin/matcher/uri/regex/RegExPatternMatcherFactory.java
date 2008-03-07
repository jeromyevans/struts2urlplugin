package com.blueskyminds.struts2.urlplugin.matcher.uri.regex;

import com.blueskyminds.struts2.urlplugin.matcher.uri.PatternMatcherFactory;
import com.blueskyminds.struts2.urlplugin.matcher.uri.PatternMatcher;

import java.util.Map;
import java.util.HashMap;

/**
 * Creates a PatternMatcher that uses RegEx
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class RegExPatternMatcherFactory implements PatternMatcherFactory {

    /** Cached PatternMatchers keyed by pattern */
    private Map<String, PatternMatcher> cachedMatchers;

    public RegExPatternMatcherFactory() {
        cachedMatchers = new HashMap<String, PatternMatcher>();
    }

    /** Gets a complied RegEx pattern */
    public PatternMatcher get(String pattern) {
        PatternMatcher patternMatcher = cachedMatchers.get(pattern);
        if (patternMatcher == null) {
            patternMatcher = new RegExPatternMatcher(pattern);
            cachedMatchers.put(pattern, patternMatcher);
        }
        return patternMatcher;
    }
}
