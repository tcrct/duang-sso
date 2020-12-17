package com.duangframework.sso.core;

import com.duangframework.sso.exceptions.SSOException;
import com.duangframework.sso.handler.SSOHandler;

/**
 * 过滤器链条
 */
public class SSOFilterChain {

    private SSOContext context;
    private int currentPosition = 0;

    public SSOFilterChain(SSOContext context) {
        this.context = context;
    }

    public void doNextFilter() throws SSOException {
        ++this.currentPosition;
        if (this.currentPosition <= SSOHandler.SSO_FILTERS.length) {
            SSOHandler.SSO_FILTERS[this.currentPosition - 1].doFilter(this.context, this);
        } else {
            SSOUserData.getInstance().setCurrentUsername(this.context.getCurrentUsername());
        }

    }

    public boolean isFinish() {
        return this.currentPosition > SSOHandler.SSO_FILTERS.length;
    }

}
