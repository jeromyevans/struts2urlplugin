package com.blueskyminds.struts2.urlplugin.matcher.uri.keyword;

import com.blueskyminds.struts2.urlplugin.matcher.uri.regex.RegExPatternMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.uri.PatternMatcherFactory;
import com.blueskyminds.struts2.urlplugin.matcher.uri.PatternMatcher;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A factory that creates the keyword patterns
 *
 * Keyword patterns are evaluated and converted to regex patterns
 *
 * Date Started: 22/02/2008
 * <p/>
 * History:
 */
public class KeywordPatternMatcherFactory implements PatternMatcherFactory {

    /** Cached PatternMatchers keyed by pattern */
    private Map<String, PatternMatcher> cachedMatchers;

    public KeywordPatternMatcherFactory() {
        cachedMatchers = new HashMap<String, PatternMatcher>();
    }

    /** Gets a complied pattern */
    public PatternMatcher get(String pattern) {
        PatternMatcher patternMatcher = cachedMatchers.get(pattern);
        if (patternMatcher == null) {
            patternMatcher = new KeywordPatternMatcher(pattern);
            cachedMatchers.put(pattern, patternMatcher);
        }
        return patternMatcher;
    }

}
