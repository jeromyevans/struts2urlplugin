package com.blueskyminds.struts2.urlplugin.matcher.action.namespace;

import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

/**
 * Matches a Struts2 package namespace against a pattern
 */
public interface NamespaceMatcher {

    /**
     * Determines whether the input namespace matches the candidate namespace
     *
     * @param inputNamespace        the input namespace (eg. from a matched URI)
     * @param candidateNamespace    the candidate namespace (eg. for a package)
     * @param matchContext          the context may be used and/or updated if relevant
     * @return
     */
    boolean match(String inputNamespace, String candidateNamespace, MatchContext matchContext);
}
