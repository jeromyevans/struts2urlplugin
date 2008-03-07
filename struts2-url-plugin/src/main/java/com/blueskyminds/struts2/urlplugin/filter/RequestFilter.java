package com.blueskyminds.struts2.urlplugin.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * Filters which Requests are processed by the ActionMapper
 */
public interface RequestFilter {

    /**
     *  Initialise a parameter of the filter
     **/
    void setParam(String name, String value);

    /**
     * Filter the input request
     *
     * @param servletRequest
     * @return true if it should be processed by the ActionMapper
     */
    boolean accept(HttpServletRequest servletRequest);
}
