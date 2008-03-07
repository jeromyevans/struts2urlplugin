package com.blueskyminds.struts2.urlplugin.matcher.action.namespace;

import junit.framework.TestCase;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

/**
 * Date Started: 21/02/2008
 * <p/>
 * History:
 */
public class TestNamedVariableNamespaceMatcher extends TestCase {

    public void testNamespaceVariables() {
        NamedVariableNamespaceMatcher matcher = new NamedVariableNamespaceMatcher();
        MatchContext matchContext = new MatchContext();

        assertTrue(matcher.match("", "", matchContext));
        assertTrue(matcher.match("/", "/", matchContext));
        assertTrue(matcher.match("/example", "/example", matchContext));
        assertTrue(matcher.match("/example/", "/example/", matchContext));
        assertTrue(matcher.match("/example/", "/{var1}/", matchContext));
        assertEquals("example", matchContext.get("var1"));

        matchContext = new MatchContext();
        assertFalse(matcher.match("/example/", "/{var1}/{var2}/test/{var3}/", matchContext));
        assertFalse(matcher.match("/au/nsw/test/", "/{var1}/{var2}/test/{var3}/", matchContext));
        assertTrue(matcher.match("/au/nsw/test/test1/", "/{var1}/{var2}/test/{var3}/", matchContext));
        assertEquals("au", matchContext.get("var1"));
        assertEquals("nsw", matchContext.get("var2"));
        assertEquals("test1", matchContext.get("var3"));
    }
}
