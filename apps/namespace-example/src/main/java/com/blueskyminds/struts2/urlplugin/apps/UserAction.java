package com.blueskyminds.struts2.urlplugin.apps;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Date Started: 21/02/2008
 * <p/>
 * History:
 */
public class UserAction extends ActionSupport {

    private String userId;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}