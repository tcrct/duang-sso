package com.duangframework.sso.handler;

import com.duangframework.exception.MvcException;
import com.duangframework.kit.Prop;
import com.duangframework.mvc.http.HttpSession;
import com.duangframework.mvc.http.HttpSessionManager;
import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;
import com.duangframework.mvc.http.handler.IHandler;
import com.duangframework.sso.common.Const;
import com.duangframework.sso.core.SSOContext;
import com.duangframework.sso.core.SSOFilter;
import com.duangframework.sso.core.SSOFilterChain;
import com.duangframework.sso.exceptions.SSOException;
import com.duangframework.sso.filter.AbstractFilter;
import com.duangframework.sso.utils.CommonUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SSOHandler implements IHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSOHandler.class);

    private String filterConfigFile;
    public static SSOFilter[] SSO_FILTERS;
    private static Properties prop = null;

    public SSOHandler() {
        this(Const.FILTER_CONFIG_FILE);
    }

    public SSOHandler(String filterConfigFile) {
        this.filterConfigFile = filterConfigFile;
        init();
    }

    private void init() {
        prop = new Prop(filterConfigFile).getProperties();
        String filterChain = prop.getProperty("filter.chain");
        if (filterChain != null && filterChain.length() > 0) {
            String[] filterClassFullNames = filterChain.split(";");
            SSO_FILTERS = new SSOFilter[filterClassFullNames.length];
            String packageName = AbstractFilter.class.getPackage().getName() + ".";

            for(int i = 0; i < filterClassFullNames.length; ++i) {
                String clzName = filterClassFullNames[i];
                if (clzName.indexOf(".") == -1) {
                    clzName = packageName + clzName;
                }

                try {
                    Class c = Class.forName(clzName);
                    SSOFilter filter = (SSOFilter)c.newInstance();
                    SSO_FILTERS[i] = filter;
                    filter.init(prop);
                } catch (Exception e) {
                    LOGGER.warn("加载SSO过滤器[{}]时发生错误！", clzName, e);
                    throw new MvcException(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void doHandler(String target, IRequest request, IResponse response) throws MvcException {

        // 转换成 Servlet 的 Request
        if (SSO_FILTERS != null && SSO_FILTERS.length != 0) {
            HttpSession session = request.getSession();
            if (null == session) {
                LOGGER.warn("无法获取Session对象，SSO客户端无法正常运行！");
            }
            else {
                Map parameter = (Map) session.getAttribute(SSOHandler.class.getName());
                if (parameter == null) {
                    parameter = new HashMap();
                    session.setAttribute(SSOHandler.class.getName(), parameter);
                }

                String username = (String) ((Map) parameter).get(Const.SSO_USER_NAME_FIELD);
                SSOContext context = SSOContext.initThreadLocal(username, parameter, request, response);

                try {
                    SSOFilterChain ssoChain = new SSOFilterChain(context);
                    ssoChain.doNextFilter();
                    if (ssoChain.isFinish()) {
                        ClientAuthenticationHandler authenticationHandler = new ClientAuthenticationHandler(prop);
                        authenticationHandler.doHandler(target, request, response);
                        context.setParameter(Const.SSO_USER_NAME_FIELD, context.getCurrentUsername());
                    }
                } catch (SSOException ssoException) {
                    throw ssoException;
                } finally {
                    SSOContext.removeThreadLocal();
                }

            }
        }
    }
}
