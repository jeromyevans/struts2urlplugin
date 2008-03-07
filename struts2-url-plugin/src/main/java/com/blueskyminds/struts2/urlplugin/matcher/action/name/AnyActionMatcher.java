package com.blueskyminds.struts2.urlplugin.matcher.action.name;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.blueskyminds.struts2.urlplugin.configuration.ActionSelector;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

/**
 * Matches ANY action name to an ActionConfig
 */
public class AnyActionMatcher implements ActionNameMatcher {

    /**
     * Always creates a successful match
     *
     * @param inputName      the input action name (eg. from a matched URI with variables substituted)
     * @param actionName     the candidate action name specified in the PackageConfig
     * @param actionConfig   the candidate action's config
     * @param actionSelector the selector currently being used
     * @param matchContext   the match context
     * @param packageConfig  the config for the package that contains the candidate action
     * @return and ActionMapping derived from the ActionConfig and matchContext
     */
    public ActionMapping match(String inputName, String actionName, ActionConfig actionConfig, ActionSelector actionSelector, MatchContext matchContext, PackageConfig packageConfig) {
        return new ActionMapping(actionName, packageConfig.getNamespace(), actionSelector.getMethod(), null);
    }
}
