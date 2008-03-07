package com.blueskyminds.struts2.urlplugin.matcher.action.name;

import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;
import com.blueskyminds.struts2.urlplugin.configuration.ActionSelector;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

/**
 * An action name matcher that uses exact plain text equality
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class PlainTextActionNameMatcher implements ActionNameMatcher {

    public static final String DEFAULT_NAME = "plainText";

    /** Create an action name matcher that uses exact plain text equality */
    public PlainTextActionNameMatcher() {
    }

    /**
     * Determines whether the input action name matches the candidate action name
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
                actionMapping = new ActionMapping(actionName, packageConfig.getNamespace(), actionSelector.getMethod(), null);
            }
        }
        return actionMapping;
    }

}
