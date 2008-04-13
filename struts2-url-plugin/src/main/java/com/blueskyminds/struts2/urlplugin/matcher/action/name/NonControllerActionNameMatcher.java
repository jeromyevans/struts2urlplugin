package com.blueskyminds.struts2.urlplugin.matcher.action.name;

import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;
import com.blueskyminds.struts2.urlplugin.configuration.ActionSelector;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

/**
 * An ActionNameMatcher that uses plainText matching but checks the action's classname to ensure it doesn't end with "Controller"
 *
 * Date Started: 13/04/2008
 */
public class NonControllerActionNameMatcher implements ActionNameMatcher {

    public static final String DEFAULT_NAME = "nonController";

    private static final String CONTROLLER_SUFFIX = "Controller";

    /** Create an action name matcher that uses exact plain text equality but ensures the action classname doesn't end with "Controller" */
    public NonControllerActionNameMatcher() {
    }

    /**
     * Determines whether the input action name matches the candidate action name AND the action classname doesn't end with "Controller"
     *
     * @param inputName      the input action name (eg. from a matched URI with variables substituted)
     * @param actionName     the candidate action name specified in the PackageConfig
     * @param actionConfig   the candidate action's config
     * @param actionSelector the selector currently being used
     * @param matchContext   the match context
     * @return and ActionMapping derived from the ActionConfig and matchContext
     */
    public ActionMapping match(String inputName, String actionName, ActionConfig actionConfig, ActionSelector actionSelector, MatchContext matchContext, PackageConfig packageConfig) {
        ActionMapping actionMapping = null;
        if (inputName != null) {
            if (inputName.equals(actionName)) {
                if (!actionConfig.getClassName().endsWith(CONTROLLER_SUFFIX)) {
                    actionMapping = new ActionMapping(actionName, packageConfig.getNamespace(), actionSelector.getMethod(), null);
                }
            }
        }
        return actionMapping;
    }
}