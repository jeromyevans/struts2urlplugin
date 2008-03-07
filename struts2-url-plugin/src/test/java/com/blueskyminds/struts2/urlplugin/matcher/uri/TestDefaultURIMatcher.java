package com.blueskyminds.struts2.urlplugin.matcher.uri;

import junit.framework.TestCase;
import com.blueskyminds.struts2.urlplugin.configuration.URIPattern;
import com.blueskyminds.struts2.urlplugin.utils.ComponentURI;
import com.blueskyminds.struts2.urlplugin.matcher.uri.DefaultURIMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

/**
 * Runs some unit tests through the DefaultURIMatcher
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class TestDefaultURIMatcher extends TestCase {

    private DefaultURIMatcher uriMatcher;
    private MatchContext matchContext;

    protected void setUp() throws Exception {
        super.setUp();

        uriMatcher = new DefaultURIMatcher();
        matchContext = new MatchContext();
    }

    public void testMatch() {
        URIPattern uriPattern = new URIPattern("1", "regex", "GET", ".*");

        assertFalse(uriMatcher.matchesMethod(new ComponentURI("get", "/", null), uriPattern, matchContext));
        assertTrue(uriMatcher.matchesMethod(new ComponentURI("GET", "/", null), uriPattern, matchContext));
        assertFalse(uriMatcher.matchesMethod(new ComponentURI("post", "/", null), uriPattern, matchContext));
        assertFalse(uriMatcher.matchesMethod(new ComponentURI(null, "/", null), uriPattern, matchContext));

        assertTrue(uriMatcher.matchesPath(new ComponentURI("GET", "/", null), uriPattern, matchContext));
        assertTrue(uriMatcher.matchesPath(new ComponentURI("GET", "", null), uriPattern, matchContext));
        assertTrue(uriMatcher.matchesPath(new ComponentURI("GET", "/example", null), uriPattern, matchContext));
        assertTrue(uriMatcher.matchesPath(new ComponentURI("GET", "/example/", null), uriPattern, matchContext));
    }

}
