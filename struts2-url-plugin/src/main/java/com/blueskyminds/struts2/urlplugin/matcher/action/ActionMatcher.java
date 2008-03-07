package com.blueskyminds.struts2.urlplugin.matcher.action;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.blueskyminds.struts2.urlplugin.configuration.ActionSelector;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;
import com.opensymphony.xwork2.config.Configuration;

/**
 * An ActionMatcher can match an Action within a Struts2 Configuration using the selector and context
 *
 */
public interface ActionMatcher {

    /**
     * Searches for an action in the configuration that matches the ActionSelector within the given MatchContext
     */
    ActionMapping match(ActionSelector actionSelector, MatchContext matchContext, Configuration configuration);
}
