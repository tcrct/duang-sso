package com.duangframework.sso.core;

import com.duangframework.sso.common.Const;
import com.duangframework.sso.handler.SSOHandler;

import java.io.Serializable;

public class SSOUserData implements Serializable {
    private static final long serialVersionUID = -4963191787310836638L;

    /**
     * 当前用户名
     */
    private String currentUsername;
    /**
     * 原来的用户名
     */
    private String originUsername;

    public static SSOUserData getInstance() {
        SSOContext context = SSOContext.getInstance();
        SSOUserData data = (SSOUserData)context.getParameter(Const.SSO_USER_DATA_FIELD);
        if (data == null) {
            data = new SSOUserData();
            context.setParameter(Const.SSO_USER_DATA_FIELD, data);
        }

        return data;
    }

    private SSOUserData() {
    }

    public void acceptUserChange() {
        this.originUsername = this.currentUsername;
    }

    public void changeCurrentUser(String username) {
        this.originUsername = username;
        this.currentUsername = username;
        SSOContext context = SSOContext.getInstance();
        context.setCurrentUsername(username);
        if (context.isUserChanged() && SSOHandler.SSO_FILTERS != null) {
            for(int i = SSOHandler.SSO_FILTERS.length - 1; i >= 0; --i) {
                SSOHandler.SSO_FILTERS[i].onAppChangeUser(context);
            }
        }

    }

    public String getCurrentUsername() {
        return this.currentUsername;
    }

    public String getOriginUsername() {
        return this.originUsername;
    }

    public boolean isUserChanged() {
        return !this.currentUsername.equalsIgnoreCase(this.originUsername);
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

}
