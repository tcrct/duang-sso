package com.duangframework.sso.filter;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;
import com.duangframework.sso.common.Const;
import com.duangframework.sso.core.SSOContext;
import com.duangframework.sso.core.SSOFilterChain;
import com.duangframework.sso.exceptions.SSOException;
import com.duangframework.sso.utils.CommonUtils2;

import java.net.URLEncoder;
import java.util.Properties;

public class LoginRedirectFilter extends AbstractFilter {
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
        boolean needRedirect = ToolsKit.isEmpty(username);
        if (needRedirect) {
            needRedirect = !CommonUtils2.checkPath(request, this.noRedirectURLs);
        }

        if (needRedirect) {
            String requestURL = request.getRequestURL();
            String queryString = request.getQueryString();
            if (ToolsKit.isNotEmpty(queryString)) {
                requestURL = requestURL + "?" + queryString;
            }

            if (ToolsKit.isNotEmpty(this.URLCharset)) {
                try {
                    requestURL = URLEncoder.encode(requestURL, this.URLCharset);
                } catch (Exception e) {
                    throw new SSOException(e.getMessage(), e);
                }
            }

            String redirectTo = this.loginURL.replace("${URL}", requestURL);
            response.redirect(redirectTo);
            return;
        } else {
            chain.doNextFilter();
        }
    }
}