package com.blueskyminds.struts2.urlplugin.utils;

/**
 * A URI that has been pre-parsed into its components (without all the fluff)
 */
public class ComponentURI {

    private String method;
    private String path;
    private String query;

    public ComponentURI(String method, String path, String query) {
        this.method = method;
        this.path = path;
        this.query = query;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String getFile() {
        if (path.indexOf("/") >= 0) {
            return path.substring(path.lastIndexOf("/")+1);
        } else {
            return path;
        }
    }

    public String getExtension() {
        if (path.indexOf(".") >= 0) {
            return path.substring(path.lastIndexOf("."));
        } else {
            return "";
        }
    }

    public String toString() {
        return "ComponentURI[method:"+method+" path:"+path+" query:"+query+"]";
    }
}
