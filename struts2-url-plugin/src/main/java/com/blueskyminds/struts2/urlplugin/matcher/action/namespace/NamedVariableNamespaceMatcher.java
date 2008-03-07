package com.blueskyminds.struts2.urlplugin.matcher.action.namespace;

import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * The NamedVariableNamespaceMatcher allows
 *
 * Borrowed from the XWork 2.1 NamedVariablePatternMatcher
 *
 * Date Started: 29/01/2008
 * <p/>
 * History:
 */
public class NamedVariableNamespaceMatcher implements NamespaceMatcher {

    /**
     * Determines whether the input namespace matches the candidate namespace allowing for named variables
     *  in the namespace
     *
     * @param inputNamespace     the input namespace (eg. from a matched URI)
     * @param candidateNamespace the candidate namespace (eg. for a package)
     * @param matchContext       the context may be used and/or updated if relevant
     * @return
     */
    public boolean match(String inputNamespace, String candidateNamespace, MatchContext matchContext) {
        if (!isLiteral(candidateNamespace)) {
            CompiledPattern pattern = compilePattern(candidateNamespace);
            return match(matchContext, inputNamespace, pattern);
        } else {
            return inputNamespace.equals(candidateNamespace);
        }
    }

    private boolean isLiteral(String pattern) {
        return (pattern == null || pattern.indexOf('{') == -1);
    }

    /**
     * Compiles the pattern.
     *
     * @param data The pattern, must not be null or empty
     * @return The compiled pattern, null if the pattern was null or empty
     */
    private CompiledPattern compilePattern(String data) {
        StringBuilder regex = new StringBuilder();
        if (data != null && data.length() > 0) {
            List<String> varNames = new ArrayList<String>();
            StringBuilder varName = null;
            for (int x=0; x<data.length(); x++) {
                char c = data.charAt(x);
                switch (c) {
                    case '{' :  varName = new StringBuilder(); break;
                    case '}' :  varNames.add(varName.toString());
                                regex.append("([^/]+)");
                                varName = null;
                                break;
                    default  :  if (varName == null) {
                                    regex.append(c);
                                } else {
                                    varName.append(c);
                                }
                }
            }
            return new CompiledPattern(Pattern.compile(regex.toString()), varNames);
        }
        return null;
    }

    /**
     * Tries to process the data against the compiled expression.  If successful, the map will contain
     * the matched data, using the specified variable names in the original pattern.
     *
     * @param matchContext
     * @param data The data to match
     * @param expr The compiled pattern
     * @return True if matched, false if not matched, the data was null, or the data was an empty string
     */
    private boolean match(MatchContext matchContext, String data, CompiledPattern expr) {

        if (data != null && data.length() > 0) {
            Matcher matcher = expr.getPattern().matcher(data);
            if (matcher.matches()) {
                for (int x=0; x<expr.getVariableNames().size(); x++)  {
                    String name = expr.getVariableNames().get(x);
                    matchContext.put(name, matcher.group(x+1));
                    // variables in the namespace will be applied as properties of the action
                    matchContext.addProperty(name, "${"+name+"}");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Stores the compiled pattern and the variable names matches will correspond to.
     */
    private static class CompiledPattern {
        private Pattern pattern;
        private List<String> variableNames;


        public CompiledPattern(Pattern pattern, List<String> variableNames) {
            this.pattern = pattern;
            this.variableNames = variableNames;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public List<String> getVariableNames() {
            return variableNames;
        }
    }
}
