package com.blueskyminds.struts2.urlplugin.matcher.uri.keyword;

import com.blueskyminds.struts2.urlplugin.matcher.uri.PatternMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A PatternMatcher that uses keywords for the commonly used patterns
 * The keywords are converted to regex and compiled, but unlike the RegExPatternMatcher the group numbers
 *  are predefined by the keyword and its position in the expression
 *
 * Date Started: 23/02/2008
 * <p/>
 * History:
 */
public class KeywordPatternMatcher implements PatternMatcher {

    public static final String DEFAULT_NAME = "keyword";

    /** Match zero or more consecutive alphanumeric characters.  The target is the entire group */
    private static final PatternSpec SLURP_TEXT = new PatternSpec("([\\w|\\d|\\-]*)", 1, 1);

    /** Match zero or one extensions consisting of a dot . and 0 or more trailing characters.
     * The inner group is the one of interest */
    private static final PatternSpec OPTIONAL_EXTENSION = new PatternSpec("((\\..*){0,1})", 2, 2);

    /**
     * An optional leading / followed by one or more characters (including slashes)
     **/
    private static final PatternSpec NAMESPACE = new PatternSpec("(/{0,1}.+)", 1, 1);

    private static final Pattern CHARS_TO_ESCAPE = Pattern.compile("([\\W&&\\D])");

    private static final Map<String, PatternSpec> PATTERNS;
    private static final Set<Pattern> SEPARATORS;

    static {
        PATTERNS = new HashMap<String, PatternSpec>();
        PATTERNS.put("namespace", NAMESPACE);
        PATTERNS.put("action", SLURP_TEXT);
        PATTERNS.put("[ext]", OPTIONAL_EXTENSION);
        PATTERNS.put("id", SLURP_TEXT);

        SEPARATORS = new HashSet<Pattern>();
        for (String key : PATTERNS.keySet()) {
            SEPARATORS.add(Pattern.compile(escapeForRegEx(key)));
        }
    }

    private String keywordPattern;
    private Pattern regExPattern;
    private Map<String, Integer> keywordGroupMap;


    public KeywordPatternMatcher(String keywordPattern) {
        this.keywordPattern = keywordPattern;
        keywordGroupMap = new HashMap<String, Integer>();
        
        if (keywordPattern != null) {
            this.regExPattern = Pattern.compile(convertToRegEx(keywordPattern));
        } else {
            regExPattern = null;
        }
    }

    /**
     * Matches the inputSequence against the pattern
     *
     * If successful, adds the matched parameters to the MatchContext by keyword
     *
     * @param inputSequence the sequence to check.  A null value can match a null pattern
     * @return true if successfully matched
     */
    public boolean matches(String inputSequence, MatchContext matchContext) {
        boolean matched = false;
        if (inputSequence != null) {
            if (regExPattern != null) {
                Matcher matcher = regExPattern.matcher(inputSequence);

                if (matcher.find()) {

                    // use the keywordGroupMap to extract the values from the regular expression and place
                    // them as parameters in the MatchContext
                    for (Map.Entry<String, Integer> entry : keywordGroupMap.entrySet()) {
                        if (entry.getValue() != null) {
                            if (entry.getValue() < matcher.groupCount()) {
                                matchContext.put(entry.getKey(), matcher.group(entry.getValue()));
                            }
                        }
                    }
                    matched = true;
                }
            } else {
                // full match - one group
                matched = true;
            }
        } else {
            if (regExPattern == null) {
                matched = true;
            }
        }

        return matched;
    }

    /**
     * Replaces keywords in the pattern with their regex equivalents and records which regex group numbers
     * match which keyword
     *
     * @param keywordPattern
     * @return
     */
    private String convertToRegEx(String keywordPattern) {
        StringBuilder result = new StringBuilder();
        String inputString = keywordPattern;
        boolean tokenFound = true;
        int baseGroupNo = 0;

        while (tokenFound) {
            String[] components = splitPreservingSeparators(inputString, SEPARATORS);
            if (components.length > 1) {
                PatternSpec patternSpec = PATTERNS.get(components[1]);
                result.append(escapeForRegEx(components[0]));
                result.append(patternSpec.getPattern());
                inputString = components[2];

                // remember which group number contains the value
                keywordGroupMap.put(components[1], baseGroupNo+patternSpec.getTargetGroup());
                baseGroupNo += patternSpec.getTotalGroups();
            } else {
                result.append(escapeForRegEx(components[0]));
                tokenFound = false;
            }
        }

        return result.toString();
    }

    private static String escapeForRegEx(String plainText) {
//        String result = plainText;
        if ((plainText != null) && (plainText.length() > 0)) {
            return CHARS_TO_ESCAPE.matcher(plainText).replaceAll("\\\\$1");        
        } else {
            return plainText;
        }
//        result = result.replace("\\", "\\\\");
//        result = result.replace("[", "\\[");
//        result = result.replace("]", "\\]");
//        result = result.replace("(", "\\(");
//        result = result.replace(")", "\\)");
//        result = result.replace("{", "\\{");
//        result = result.replace("}", "\\}");
//        result = result.replace(".", "\\.");
//        result = result.replace("*", "\\*");
//        result = result.replace("?", "\\?");
//        result = result.replace("^", "\\^");
//        result = result.replace("^", "\\^");

//        return result;
    }

    /**
     * Splits the input string into 3 parts:
     *    test left of the separator;
     *    separator;
     *    text right of the separator
     *
     * where the separator is the left-most matching pattern in the list
     *
     * If the separator is not present there will only be one part
     *
     * @param inputString
     * @param separators
     * @return an array of strings containing either:
     *    1 element equal to the input stream if no separate was found; or
     *    3 elements [leftText, separator, righttText]
     */
    private String[] splitPreservingSeparators(String inputString, Collection<Pattern> separators) {

        String[] parts;
        int leftMostMatch = Integer.MAX_VALUE;
        int rightIndex = Integer.MAX_VALUE;
        boolean anyMatch = false;

        for (Pattern pattern : separators) {
            Matcher matcher = pattern.matcher(inputString);

            if (matcher.find()) {
                int index = matcher.start();
                if (index < leftMostMatch) {
                    leftMostMatch = index;
                    rightIndex = matcher.end();
                    anyMatch = true;
                }
            }
        }

        if (anyMatch) {
            parts = new String[3];
            if (leftMostMatch > 0) {
                parts[0] = inputString.substring(0, leftMostMatch);
            } else {
                parts[0] = "";
            }
            parts[1] = inputString.substring(leftMostMatch, rightIndex);
            parts[2] = inputString.substring(rightIndex);
        } else {
            // no matches
            parts = new String[1];
            parts[0] = inputString;
        }

        return parts;
    }

    /** Contains a regex pattern, the group number of interested and the total groups */
    private static class PatternSpec {
          private String pattern;
          private int targetGroup;
          private int totalGroups;

          private PatternSpec(String pattern, int targetGroup, int totalGroups) {
              this.pattern = pattern;
              this.targetGroup = targetGroup;
              this.totalGroups = totalGroups;
          }

        public String getPattern() {
            return pattern;
        }

        public int getTargetGroup() {
            return targetGroup;
        }

        public int getTotalGroups() {
            return totalGroups;
        }
    }

}
