package com.duangframework.sso.filter;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;
import com.duangframework.sso.common.Const;
import com.duangframework.sso.core.SSOContext;
import com.duangframework.sso.core.SSOFilterChain;
import com.duangframework.sso.exceptions.SSOException;
import com.duangframework.sso.utils.CommonUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class LoginRedirectFilter extends AbstractFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRedirectFilter.class);

    private String loginURL;
    private String noRedirectURLs;
    private String URLCharset;

    public LoginRedirectFilter() {
    }

    public void init(Properties prop) throws SSOException {
        this.loginURL = this.getConfigProperty(prop, Const.LOGIN_URL);
        this.noRedirectURLs = prop.getProperty(Const.NO_REDIRECT_URLS);
        this.URLCharset = prop.getProperty(Const.URL_CHARSET);
    }

    public void destroy() {
        this.loginURL = null;
        this.noRedirectURLs = null;
        this.URLCharset = null;
    }

    public void doFilter(SSOContext context, SSOFilterChain chain) throws SSOException {
        IRequest request = context.getRequest();
        IResponse response = context.getResponse();
        String username = context.getCurrentUsername();
        // 根据username是否为空判断是否创建重定向URL，如果重定向URL不为空，则重定向
        String redirectTo = CommonUtils2.createRedirectUrl(request, this.loginURL, this.noRedirectURLs, username, this.URLCharset);
        if (ToolsKit.isNotEmpty(redirectTo)) {
            LOGGER.warn("redirect url: {}", redirectTo);
            response.redirect(redirectTo);
            //中止线程往下执行
            throw new SSOException("redirect url");
        } else {
            chain.doNextFilter();
        }
    }
}