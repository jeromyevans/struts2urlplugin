package com.blueskyminds.struts2.urlplugin.matcher.action;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.struts2.urlplugin.configuration.ActionSelector;
import com.blueskyminds.struts2.urlplugin.matcher.action.namespace.NamespaceMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.action.name.ActionNameMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.action.MatcherProvider;
import com.blueskyminds.struts2.urlplugin.matcher.action.ActionMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.inject.Container;

import java.util.Map;

/**
 * Matches an ActionSelector to an action within a Struts2 Configuration using the matchers provided by the
 * injected MatcherProvider
 */
public class DefaultActionMatcher implements ActionMatcher {

    private static final Log LOG = LogFactory.getLog(DefaultActionMatcher.class);

    private Container container;
    private MatcherProvider<NamespaceMatcher> namespaceMatcherProvider;
    private MatcherProvider<ActionNameMatcher> actionMatcherProvider;

    public DefaultActionMatcher(MatcherProvider<NamespaceMatcher> namespaceMatcherProvider, MatcherProvider<ActionNameMatcher> actionMatcherProvider) {
        this.namespaceMatcherProvider = namespaceMatcherProvider;
        this.actionMatcherProvider = actionMatcherProvider;
    }

    /** Create a new ActionMapper.  A MatcherProvider needs to be injected */
    public DefaultActionMatcher() {
    }

    /**
     * Searches for an action in the configuration that matches ActionSelector
     *
     * First searches for a package with a matching namespace, then searches for an
     *  action with a matching action name
     * 
     */
    public ActionMapping match(ActionSelector actionSelector, MatchContext matchContext, Configuration configuration) {
       ActionMapping actionMapping = null;
        String actionName;
        String packageNamespace;
        PackageConfig packageConfig;
        ActionConfig actionConfig;

        setupConfiguration();

        // iterate through all the package configs...
        Map<String, PackageConfig> packageConfigs = configuration.getPackageConfigs();
        for (Map.Entry<String, PackageConfig> packageEntry : packageConfigs.entrySet()) {
            packageConfig = packageEntry.getValue();
            packageNamespace = packageConfig.getNamespace();

            // check if the namespace matches...
            if (matchNamespace(actionSelector, matchContext, packageNamespace)) {
                Map<String, ActionConfig> actionsConfigs = packageConfig.getActionConfigs();

                // check if any of the actions match...
                for (Map.Entry<String, ActionConfig> actionEntry : actionsConfigs.entrySet()) {
                    actionName = actionEntry.getKey();
                    actionConfig = actionEntry.getValue();

                    actionMapping = matchActionName(actionConfig, actionSelector, matchContext, actionName, packageConfig);
                    if (actionMapping != null) {
                        break;
                    }
                }
            }

            // break out on the first matching action
            if (actionMapping != null) {
                break;
            }
        }

        return actionMapping;
    }

    public String substituteVariables(String pattern, MatchContext matchContext) {
        String result;
        if (pattern != null) {
            result = matchContext.evaluateExpression(pattern);
            if (result.contains("$")) {
                // allow a level of nesting - this will evaluate expressions in the params of the URIMatch
                result = matchContext.evaluateExpression(result);
            }
        } else {
            result = null;
        }
        return result;
    }

    /**
     * Match the namespace pattern if defined
     *
     * @return true if there's a match or the pattern isn't defined
     **/
    protected boolean matchNamespace(ActionSelector actionSelector, MatchContext matchContext, String candidateNamespace) {
        String namespacePattern = actionSelector.getNamespace();
        if (namespacePattern != null) {
            NamespaceMatcher namespaceMatcher = namespaceMatcherProvider.getMatcher(actionSelector.getNamespaceMatcher());
            if (namespaceMatcher != null) {
                String substituted = substituteVariables(namespacePattern, matchContext);
                boolean matched = namespaceMatcher.match(substituted, candidateNamespace, matchContext);
                if (LOG.isDebugEnabled()) {
                    if (matched) {
                        LOG.debug("   Matched packageNamespace:"+candidateNamespace+" to pattern "+substituted+" using "+actionSelector.getNamespaceMatcher());
                    } else {
                        LOG.trace("   No match: packageNamespace: '"+candidateNamespace+"' to pattern '"+substituted+"' using "+actionSelector.getNamespaceMatcher());
                    }
                }
                return matched;
            } else {
                LOG.error("Unknown NamespaceMatcher specified in ActionSelector: "+ actionSelector.getNamespaceMatcher());
                LOG.error("Either the name is incorrect or the bean is not defined in struts.xml or struts-plugin.xml");
                return false;
            }
        } else {
            return true;
        }
    } 

    /**
     * Match the actionName pattern if defined
     * @return true if there's a match or the pattern isn't defined
     **/
    protected ActionMapping matchActionName(ActionConfig actionConfig, ActionSelector actionSelector, MatchContext matchContext, String actionName, PackageConfig packageConfig) {
        ActionMapping actionMapping = null;
        String actionNamePattern = actionSelector.getName();

        ActionNameMatcher actionNameMatcher = actionMatcherProvider.getMatcher(actionSelector.getActionMatcher());
        if (actionNameMatcher != null) {
            // substitute variables into the pattern if required
            String substituted = substituteVariables(actionNamePattern, matchContext);
            // and match to the action config...
            actionMapping = actionNameMatcher.match(substituted, actionName, actionConfig, actionSelector, matchContext, packageConfig);

            if (LOG.isDebugEnabled()) {
                if (actionMapping != null) {
                    LOG.debug("Matched action:"+actionName+" to pattern "+substituted+" using "+actionSelector.getActionMatcher());
                } else {
                    LOG.debug("No match for action:"+actionName+" against pattern "+substituted+" using "+actionSelector.getActionMatcher());
                }
            }
        } else {
            LOG.error("Unknown ActionNameMatcher specified in ActionSelector: "+ actionSelector.getActionMatcher());
        }

        return actionMapping;
    }

    /** Setup the container as a provider of Matchers if they haven't been injected by any other means */
    private void setupConfiguration() {
     if (container != null) {
            if (namespaceMatcherProvider == null) {
                namespaceMatcherProvider = new MatcherProvider<NamespaceMatcher>() {
                    public NamespaceMatcher getMatcher(String id) {
                        return container.getInstance(NamespaceMatcher.class, id);
                    }
                };
            }

         if (actionMatcherProvider == null) {
                actionMatcherProvider = new MatcherProvider<ActionNameMatcher>() {
                    public ActionNameMatcher getMatcher(String id) {
                        return container.getInstance(ActionNameMatcher.class, id);
                    }
                };
            }
        }
    }


    @Inject
    public void setContainer(Container container) {
        this.container = container;
    }
}
