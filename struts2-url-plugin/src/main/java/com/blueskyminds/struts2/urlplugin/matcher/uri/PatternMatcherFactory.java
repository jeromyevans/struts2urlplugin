package com.blueskyminds.struts2.urlplugin.matcher.uri;

/**
 * Creates PatternMatcher instances
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public interface PatternMatcherFactory {

    /** Get a PatternMatcher instance for the specified pattern. */
    PatternMatcher get(String pattern);
}
