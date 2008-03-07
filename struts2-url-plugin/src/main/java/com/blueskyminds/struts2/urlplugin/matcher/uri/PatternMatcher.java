package com.blueskyminds.struts2.urlplugin.matcher.uri;

import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

import java.util.List;

/**
 * Simple interface to a pattern matching algorithm
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public interface PatternMatcher {

    /**
     * Matches the inputSequence against the pattern
     *
     * @param inputSequence     the sequence to check.  A null value can match a null pattern
     *
     * @return a list of grouped matched, where:
     *    an empty list implies no match;
     *    list item zero contains the full match (as per java.util.regex.Matcher)
     *    list item 1..n contains group matches (as per java.util.regex.Matcher@group)
     *
     * The match value at index 0 implies a null value was matched to a null pattern
     *
     * The list itself is NEVER null
     */
    boolean matches(String inputSequence, MatchContext matchContext);
}
