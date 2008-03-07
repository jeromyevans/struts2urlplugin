package com.blueskyminds.struts2.urlplugin.matcher.uri.regex;

import com.blueskyminds.struts2.urlplugin.matcher.uri.PatternMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Performs a match using a compiled regular expression
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class RegExPatternMatcher implements PatternMatcher {

    private Pattern pattern;

    public RegExPatternMatcher(String regEx) {
        if (regEx != null) {
            this.pattern = Pattern.compile(regEx);
        } else {
            pattern = null;
        }
    }

    /**
     * Matches the inputSequence against the pattern.
     *
     * If there is a match, the regex groups are added to the MatchContext
     *
     * @param inputSequence the sequence to check.  A null value can match a null pattern
     * @return true if a successful match
     */
    public boolean matches(String inputSequence, MatchContext matchContext) {
        boolean matched = false;

        if (inputSequence != null) {
            if (pattern != null) {
                Matcher matcher = pattern.matcher(inputSequence);

                if (matcher.find()) {
                    // map all of the other groups into the context
                    if (matcher.groupCount() > 0) {
                        // add group 0 to the context (full string)
                        matchContext.addGroup(matcher.group(0));

                        for (int group = 1; group <= matcher.groupCount(); group++) {
                            matchContext.addGroup(matcher.group(group));
                        }
                    }
                    matched = true;
                }
            } else {
                // full match - one group (whole string) so we won't add it
                matched = true;
                //matchContext.addGroup(inputSequence);
            }
        } else {
            if (pattern == null) {
                // matched a null input to a null value
                matched = true;
            }
        }

        return matched;
    }
}
