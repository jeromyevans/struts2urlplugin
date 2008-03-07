package com.blueskyminds.struts2.urlplugin.matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.blueskyminds.struts2.urlplugin.expression.ExpressionProcessor;

/**
 * Context collected by the matching process
 *
 * The implementation is a Map of name=value pairs, a separate ordered list of string values
 *  resulting from group matches (regex groups) and property values to be applied to the target action
 *
 * The context can also evaluate cross-references to names or groups.
 *   eg. ${name} is a reference to a parameter called name
 *       $1 is a reference to group #1  
 */
public class MatchContext {

    private static final Log LOG = LogFactory.getLog(MatchContext.class);

    private ExpressionProcessor expressionProcessor;

    private Map<String, String> params;
    private List<String> groups;
    private String namespace;
    private Map<String, String> properties;

    public MatchContext() {
        params = new HashMap<String, String>();
        groups = new ArrayList<String>(10);
        properties = new HashMap<String, String>();
        expressionProcessor = new ExpressionProcessor(new MatchContextExpressionListener(this));
    }

    public void put(String key, String value) {
        params.put(key, value);
    }

    public void putAll(Map<String, String> paramsToAdd) {
        this.params.putAll(paramsToAdd);
    }

    public String get(String key) {
        return params.get(key);
    }

    public void addGroup(String value) {
        groups.add(value);
    }

    public String getGroup(int index) {
        if ((index >= 0) && (index < groups.size())) {
            return groups.get(index);
        } else {
            return null;
        }
    }

    public List<String> getGroups() {
        return groups;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
        put(MatcherConstants.NAMESPACE, namespace);
    }

    /**
     * Defines a property value that should be set on the target action
     *
     * @param key
     * @param expression  reference to a value in the context or literal
     */
    public void addProperty(String key, String expression) {
        properties.put(key, expression);
    }

    /**
     * The properties are intended to be set on the target action
     *
     * @return a new instance of the map of properties and the values
     * */
    public Map<String, String> evaluateProperties() {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            result.put(entry.getKey(), expressionProcessor.evaluate(entry.getValue()));
        }
        return result;
    }

    public String evaluateExpression(String expression) {
        return expressionProcessor.evaluate(expression);
    }

    /**
     * @return true if 1 or more properties have been defined by this match
     */
    public boolean isPropertiesDefined() {
        return properties.size() > 0;
    }

    public String toString() {
        boolean first;

        StringBuilder result = new StringBuilder();
        result.append("\n{\n");
        result.append("  params = {\n");
        first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                result.append(",\n");
            } else {
                first = false;
            }
            result.append("    "+entry.getKey()+":"+entry.getValue());
        }
        result.append("\n  },\n");
        result.append("  groups = [");

        first = true;
        for (String entry : groups) {
            if (!first) {
                result.append(",");
            } else {
                first = false;
            }
            result.append(entry);
        }
        result.append("]\n");
        result.append("}\n");
        return result.toString();
    }
}
