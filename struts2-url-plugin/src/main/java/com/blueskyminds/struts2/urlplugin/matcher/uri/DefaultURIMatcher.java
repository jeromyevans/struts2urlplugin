package com.blueskyminds.struts2.urlplugin.matcher.uri;

import com.blueskyminds.struts2.urlplugin.configuration.URIPattern;
import com.blueskyminds.struts2.urlplugin.utils.ComponentURI;
import com.blueskyminds.struts2.urlplugin.matcher.uri.regex.RegExPatternMatcherFactory;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;
import com.blueskyminds.struts2.urlplugin.matcher.MatcherConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * Default implementation of the URIMatcher.  Matches a URI to a URIPattern
 *
 * This implementation gets PatternMatchers from a PatternMatcherFactory \
 */
public class DefaultURIMatcher implements URIMatcher {

    private static final Log LOG = LogFactory.getLog(DefaultURIMatcher.class);

    public static final String DEFAULT_NAME = "default";

    private PatternMatcherFactory methodMatcherFactory;
    private PatternMatcherFactory pathMatcherFactory;

    /** Create a new URIMatcher that uses the specified method and path matcher factories
     * @param methodMatcherFactory
     * @param pathMatcherFactory
     **/
    public DefaultURIMatcher(PatternMatcherFactory methodMatcherFactory, PatternMatcherFactory pathMatcherFactory) {
        this.methodMatcherFactory = methodMatcherFactory;
        this.pathMatcherFactory = pathMatcherFactory;
    }

    /** Create a new URIMatcher that uses the same pattern matcher factory for method and path
     * @param patternMatcherFactory  pathMatcherFactory for the method and path components */
    public DefaultURIMatcher(PatternMatcherFactory patternMatcherFactory) {
        this.methodMatcherFactory = patternMatcherFactory;
        this.pathMatcherFactory = patternMatcherFactory;
    }

    /** Create a new URIMatcher that uses RegEx matching */
    public DefaultURIMatcher() {
        this.methodMatcherFactory = new RegExPatternMatcherFactory();
        this.pathMatcherFactory = methodMatcherFactory;
    }

    /**
     * Matches the method and path components of the URI to the pattern
     *
     * @param uri         the URI parsed into its components
     * @param pattern     the pattern being checked
     * @return a MatchContext if successful, otherwise null
     */
    public boolean match(ComponentURI uri, URIPattern pattern, MatchContext matchContext) {
        boolean matches = false;

        if (matchesMethod(uri, pattern, matchContext)) {
            matches = matchesPath(uri, pattern, matchContext);
        }

        return matches;
    }

    /**
     * Matches the method component of the URI to the pattern
     *
     * @param uri           the URI parsed into its components
     * @param pattern       the pattern being checked
     * @return true if this pattern matches the method
     */
    protected boolean matchesMethod(ComponentURI uri, URIPattern pattern, MatchContext matchContext) {
        PatternMatcher methodMatcher = methodMatcherFactory.get(matchContext.evaluateExpression(pattern.getMethod()));
        if (methodMatcher != null)  {
            boolean matches = methodMatcher.matches(uri.getMethod(), matchContext);
            if (LOG.isDebugEnabled()) {
                if (matches) {
                    LOG.debug("Matched method:"+uri.getMethod()+" to pattern "+pattern.getMethod()+" (ID:"+pattern.getId()+")");
                }
            }
            return matches;
        } else {
            return true;
        }
    }

    /**
     * Matches a plain text path to the URIPattern
     *
     * @param getPath           a simple path for a simulated GET request
     * @return true if this pattern matches the path component of the URI
     */
    protected boolean matchesPath(String getPath, URIPattern pattern, MatchContext matchContext) {
        return matchesPath(new ComponentURI("GET", getPath, null), pattern, matchContext);
    }

    /**
     * Matches the path component of the URI to the pattern
     *
     * @param uri           the URI parsed into its components
     * @param pattern       the pattern being checked
     * @return true if this pattern matches the method
     **/
    protected boolean matchesPath(ComponentURI uri, URIPattern pattern, MatchContext matchContext) {
        boolean matches = false;
        PatternMatcher pathMatcher = pathMatcherFactory.get(matchContext.evaluateExpression(pattern.getPath()));
        if (pathMatcher != null) {
            matches = pathMatcher.matches(uri.getPath(), matchContext);
            if (matches) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Matched path:"+uri.getPath()+" to pattern "+pattern.getPath()+" (ID:"+pattern.getId()+")");
                }

                updateMatchContext(uri, pattern, matchContext);

                //for (String groupMatch : groupMatches) {
                //    match.addGroup(groupMatch);
                //}
            }
        } else {
            // always match
            updateMatchContext(uri, pattern, matchContext);
            matches =  true;
        }
        return matches;
    }

    /**
     * Puts some useful information about the match into the context so they can be referenced by the action
     *  selector
     **/
    protected void updateMatchContext(ComponentURI uri, URIPattern pattern, MatchContext matchContext) {
        matchContext.put(MatcherConstants.METHOD, uri.getMethod());
        matchContext.put(MatcherConstants.PATH, uri.getPath());
        matchContext.put(MatcherConstants.FILE, uri.getFile());
        matchContext.put(MatcherConstants.EXTENSION, uri.getExtension());
        matchContext.put(MatcherConstants.QUERY, uri.getQuery());

        // transfer parameters from the pattern
        for (Map.Entry<String, String> entry : pattern.getParams().entrySet()) {
            matchContext.put(entry.getKey(), entry.getValue());
        }

        matchContext.put("pattern.method", pattern.getMethod());
        matchContext.put("pattern.path", pattern.getPath());
    }
    
}
