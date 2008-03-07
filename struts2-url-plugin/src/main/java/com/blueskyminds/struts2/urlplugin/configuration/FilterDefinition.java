package com.blueskyminds.struts2.urlplugin.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines a Filter to apply to accept URLs
 */
public class FilterDefinition {

    private String name;
    private Map<String, String> parameters;

    public FilterDefinition() {
        parameters = new HashMap<String, String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParam(String name, String value) {
        parameters.put(name, value);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
