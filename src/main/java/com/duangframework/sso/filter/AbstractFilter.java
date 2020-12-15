package com.duangframework.sso.filter;

import com.duangframework.kit.ToolsKit;
import com.duangframework.sso.core.SSOContext;
import com.duangframework.sso.core.SSOFilter;
import com.duangframework.sso.exceptions.SSOException;
import java.util.Properties;

public abstract class AbstractFilter implements SSOFilter {

    public AbstractFilter() {
    }

    public void onAppChangeUser(SSOContext context) {
    }

    public String getConfigProperty(Properties prop, String key) throws SSOException {
        String value = prop.getProperty(key);
        if (ToolsKit.isEmpty(value)) {
            throw new SSOException("SSO客户端配置文件中，未配置" + key + "属性");
        } else {
            return value;
        }
    }

}
