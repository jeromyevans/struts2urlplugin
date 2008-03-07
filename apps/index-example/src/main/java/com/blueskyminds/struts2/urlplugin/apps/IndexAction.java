package com.blueskyminds.struts2.urlplugin.apps;

import com.opensymphony.xwork2.ActionSupport;

/**
 * An example action that sets a message if not already defined
 */
public class IndexAction extends ActionSupport {

    private static final String DEFAULT_MESSAGE = "Example 1 default message";

    private String message;

    public String execute() throws Exception {
        if ((message == null) || (message.length() == 0)) {
            message = DEFAULT_MESSAGE;
        }
        message = getClass().getSimpleName()+":"+message;
        return SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}