package com.duangframework.sso.core;

import com.duangframework.sso.exceptions.SSOException;

import java.util.Properties;

/**
 * SSO Filter
 *
 * @author Laotang
 * @since 1.0
 */
public interface SSOFilter {

    void init(Properties var1) throws SSOException;

    void destroy();

    void doFilter(SSOContext context, SSOFilterChain chain) throws SSOException;

    void onAppChangeUser(SSOContext context);

}
