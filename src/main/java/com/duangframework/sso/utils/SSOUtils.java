package com.duangframework.sso.utils;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.http.IRequest;
import com.duangframework.sso.common.Const;
import com.duangframework.sso.core.SSOUserData;

public class SSOUtils {

    /**
     * 是否 SSO Login 验证完成后继续的请求
     * @param request
     * @return
     */
    public static String getSSOLoginRequestUserName(IRequest request) {
        String ssoUserName = (String)request.getAttribute(Const.SSO_USERNAME);
        if (ToolsKit.isNotEmpty(ssoUserName)) {
            String accessKey = (String)request.getAttribute(Const.ACCESS_KEY);
            if (SSOUserData.getInstance().getAccessKey().equals(accessKey)) {
                return ssoUserName;
            }
        }
        return null;
    }

}
