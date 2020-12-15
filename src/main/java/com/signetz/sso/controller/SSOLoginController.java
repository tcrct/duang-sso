package com.signetz.sso.controller;

import com.duangframework.mvc.annotation.Controller;
import com.duangframework.mvc.annotation.Import;
import com.duangframework.mvc.annotation.Mapping;
import com.duangframework.mvc.core.BaseController;
import com.signetz.sso.service.SSOLoginService;

@Controller
@Mapping(value = "/")
public class SSOLoginController extends BaseController {

    @Import
    private SSOLoginService loginService;

    @Mapping(value = "/login", desc = "登录")
    public String login() {
        return String.valueOf(loginService.findById(getValue("id")));
    }

    @Mapping(value = "/logout", desc = "登出")
    public String logout() {
        return "success";
    }
}
