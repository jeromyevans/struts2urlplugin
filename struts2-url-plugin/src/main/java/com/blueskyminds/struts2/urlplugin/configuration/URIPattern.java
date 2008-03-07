package com.blueskyminds.struts2.urlplugin.configuration;

import java.util.Map;
import java.util.HashMap;

/**
 * Defines patterns for matching an HTTP URI
 *
 */
public class URIPattern {

    private String id;
    private String type;
    private String method;
    private String path;
    private Map<String, String> params;

    /**
     *
     * @param id
     * @param method
     * @param path
     */
    public URIPattern(String id, String type, String method, String path) {
        this.id = id;
        this.type = type;
        this.method = method;
        this.path = path;
        this.params = new HashMap<String, String>();
    }

    public URIPattern() {
        this.params = new HashMap<String, String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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


}
