package com.signetz.sso.controller;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.annotation.Controller;
import com.duangframework.mvc.annotation.Import;
import com.duangframework.mvc.annotation.Mapping;
import com.duangframework.mvc.core.BaseController;
import com.duangframework.sso.common.Const;
import com.signetz.sso.service.SSOLoginService;

@Controller
@Mapping(value = "/")
public class SSOLoginController extends BaseController {

    @Import
    private SSOLoginService loginService;

    @Mapping(value = "/login", desc = "登录")
    public void login() {
        try {
            String ssoUserName = (String)getRequest().getAttribute(Const.SSO_USERNAME);
            if (ToolsKit.isNotEmpty(ssoUserName)) {
                //调用 方法
            }
            returnSuccessJson(ssoUserName);
        } catch (Exception e) {
            returnFailJson(e);
        }
    }

    @Mapping(value = "/logout", desc = "登出")
    public String logout() {
        return "success";
    }
}
