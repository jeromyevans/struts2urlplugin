package com.blueskyminds.struts2.urlplugin.matcher.uri;

import com.blueskyminds.struts2.urlplugin.utils.ComponentURI;
import com.blueskyminds.struts2.urlplugin.configuration.URIPattern;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

/**
 * The URIMatcher matches a URI to a pattern
 */
public interface URIMatcher {

    /**
     * Matches the method and path components of the URI to the pattern
     *
     * Returns a MatchContext for a successful match.  The MatchContext contains data that may be relevant to
     *  the ActionMatcher such as components of the URI and RegEx groups
     *
     * @param uri
     * @param pattern
     * @return a MatchContext if successful, otherwise null
     */
    boolean match(ComponentURI uri, URIPattern pattern, MatchContext matchContext);
}
