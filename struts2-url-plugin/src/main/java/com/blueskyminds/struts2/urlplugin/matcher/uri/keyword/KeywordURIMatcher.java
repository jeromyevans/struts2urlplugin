package com.blueskyminds.struts2.urlplugin.matcher.uri.keyword;

import com.blueskyminds.struts2.urlplugin.matcher.uri.DefaultURIMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.uri.URIMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A specialisation of the DefaultURIMatcher that uses keywords for the commonly
 *   used expression
 *
 * Date Started: 22/02/2008
 * <p/>
 * History:
 */
public class KeywordURIMatcher extends DefaultURIMatcher implements URIMatcher {

    private static final Log LOG = LogFactory.getLog(KeywordURIMatcher.class);

    public static final String DEFAULT_NAME = "keyword";

    public KeywordURIMatcher() {
        super(new KeywordPatternMatcherFactory());
    }
}
