package com.blueskyminds.struts2.urlplugin.matcher.action.namespace;

import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

/**
 * A namespace matcher that uses plain text equality
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class PlainTextNamespaceMatcher implements NamespaceMatcher {

    public static final String DEFAULT_NAME = "plainText";

    /** Create a namespace matcher that uses exact plain text equality */
    public PlainTextNamespaceMatcher() {
    }

    /**
     * Performs an exact comparison between the input and target namespace
     * WITH ONE EXCEPTION: the blank namespace "" matches root "/"
     *
     * @param input
     * @param targetNamespace
     * @param matchContext      the context is not updated
     * @return
     */
    public boolean match(String input, String targetNamespace, MatchContext matchContext) {
        boolean matches = false;
        if (input != null) {
            if (targetNamespace.length() > 0) {
                matches = input.equals(targetNamespace);
            } else {
                matches = input.equals("/");
            }
        }
        return matches;
    }
}
