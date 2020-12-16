package com.signetz.sso.helper;

import com.duangframework.mvc.core.helper.BeanHelper;
import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;
import com.duangframework.sso.core.IAutoLoginHelper;

public class AutoLoginHelper implements IAutoLoginHelper {

    @Override
    public String doAutoLogin(String username, IRequest request) {
        // TODO 登录到业务系统
        //BeanHelper.getBean(LoginService.class);
        boolean isOk = true; //登录成功
        return isOk ? "success" : null;
    }

    @Override
    public void doLogout(IRequest request, IResponse response) {

    }
}
