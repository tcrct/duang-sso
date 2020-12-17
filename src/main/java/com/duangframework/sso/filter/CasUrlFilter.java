package com.duangframework.sso.filter;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;
import com.duangframework.mvc.http.session.HttpSession;
import com.duangframework.sso.common.Const;
import com.duangframework.sso.core.SSOContext;
import com.duangframework.sso.core.SSOFilterChain;
import com.duangframework.sso.exceptions.SSOException;
import com.duangframework.sso.utils.CommonUtils2;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas10TicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class CasUrlFilter extends AbstractFilter{

    private static final Logger LOGGER = LoggerFactory.getLogger(CasUrlFilter.class);

    private Map sessionMap;
    private String casServerUrlPrefix;
    private String ticketParameterName;

    public CasUrlFilter() {
    }

    public void init(Properties prop) throws SSOException {
        this.sessionMap = new ConcurrentHashMap();
        this.casServerUrlPrefix = this.getConfigProperty(prop, Const.CAS_SERVER_URL);
        this.ticketParameterName = this.getConfigProperty(prop, Const.CAS_TICKET);
    }

    public void destroy() {
        this.sessionMap = null;
        this.casServerUrlPrefix = null;
        this.ticketParameterName = null;
    }

    public void doFilter(SSOContext context, SSOFilterChain chain) throws SSOException {
        if ("POST".equalsIgnoreCase(context.getRequest().getMethod())) {
            this.doLogout(context, chain);
        } else {
            this.doLogin(context, chain);
        }

    }

    private void doLogout(SSOContext context, SSOFilterChain chain) throws SSOException {
        String logoutRequest = CommonUtils2.safeGetParameter(context.getRequest(), "logoutRequest");
        if (CommonUtils.isNotBlank(logoutRequest)) {
            String sessionIdentifier = XmlUtils.getTextForElement(logoutRequest, "SessionIndex");
            if (CommonUtils.isNotBlank(sessionIdentifier)) {
                HttpSession session = (HttpSession)this.sessionMap.remove(sessionIdentifier);
                if (session != null) {
                    try {
                        session.invalidate(); //使该Session无效
                    } catch (IllegalStateException ise) {
                        throw new SSOException(ise.getMessage(), ise);
                    }
                }
            }
        }

        chain.doNextFilter();
    }

    private void doLogin(SSOContext context, SSOFilterChain chain) throws SSOException {
        IRequest request = context.getRequest();
        IResponse response = context.getResponse();
        String ticket = request.getParameter(this.ticketParameterName);
        // ticket不为空且存在本地缓存中，则请求SSO服务器验证ticket是否合法
        if (ToolsKit.isNotEmpty(ticket) && !this.sessionMap.containsKey(ticket)) {
            String localServerName = request.getRemoteHost();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("从URL中获取到的{}参数为：{}", this.ticketParameterName, ticket);
            }
            String serviceURL = CommonUtils2.constructServiceUrl(request, response, null, localServerName, this.ticketParameterName, false);

            try {
                String username = null;
                TicketValidator validator = null;
                validator = new Cas10TicketValidator(this.casServerUrlPrefix);
                Assertion assertion = validator.validate(ticket, serviceURL);
                username = assertion.getPrincipal().getName();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("经过验证票据，得到用户名：" + username);
                }
                if (ToolsKit.isNotEmpty(username)) {
                    context.setCurrentUsername(username);
                } else {
                    ticket = null;
                }
            } catch (TicketValidationException e) {
                response.setStatus(403);
                throw new SSOException(e.getMessage(), e);
            }

            chain.doNextFilter();
            // 添加到本地缓存中
            if (chain.isFinish() && ticket != null) {
                this.sessionMap.put(ticket, request.getSession());
            }

        } else {
            chain.doNextFilter();
        }
    }
}
