package com.blueskyminds.struts2.urlplugin.configuration;

import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

import java.util.Map;
import java.util.HashMap;

/**
 * Defines patterns and matchers for selecting an action
 */
public class ActionSelector {

    private static final String DEFAULT_METHOD = "execute";
    private static final String DEFAULT_MATCHER = "plainText";

    private String namespaceMatcher;
    private String namespace;
    private String actionMatcher;
    private String name;
    private String method;
    private Map<String, String> params;

    /**
     *
     * @param namespaceMatcher   name of the namespace matcher to use
     * @param namespace          namespace pattern
     * @param actionMatcher      name of the action name matcher to use
     * @param name             action name pattern
     * @param method             action method to execute
     */
    public ActionSelector(String namespaceMatcher, String namespace, String actionMatcher, String name, String method) {
        this.namespaceMatcher = namespaceMatcher;
        this.namespace = namespace;
        this.actionMatcher = actionMatcher;
        this.name = name;
        this.method = method;
        this.params = new HashMap<String, String>();
    }

    /**
     * Use the default namespace and action matcher
     *
     * @param namespace          namespace pattern
     * @param name             action name pattern
     * @param method             action method to execute
     */
    public ActionSelector(String namespace, String name, String method) {
        this.namespace = namespace;
        this.name = name;
        this.method = method;
        this.params = new HashMap<String, String>();
    }

    public ActionSelector() {
        this.params = new HashMap<String, String>();
    }

    public String getNamespaceMatcher() {
        return defaultIfNull(namespaceMatcher, DEFAULT_MATCHER);
    }

    public void setNamespaceMatcher(String namespaceMatcher) {
        this.namespaceMatcher = namespaceMatcher;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getActionMatcher() {
        return defaultIfNull(actionMatcher, DEFAULT_MATCHER);
    }

    public void setActionMatcher(String actionMatcher) {
        this.actionMatcher = actionMatcher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return defaultIfNull(method, DEFAULT_METHOD);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    private String defaultIfNull(String value, String defaultValue) {
        if ((value != null && (value.length() > 0))) {
            return value;
        } else {
            return defaultValue;
        }
    }

    public void addParameter(String name, String value) {
        params.put(name, value);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * Evaluate expressions in the parameters and return values
     *
     * @param matchContext
     * @return
     */
    public Map<String, String> evaluateParams(MatchContext matchContext) {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.put(entry.getKey(), matchContext.evaluateExpression(entry.getValue()));
        }
        return result;
    }
}
