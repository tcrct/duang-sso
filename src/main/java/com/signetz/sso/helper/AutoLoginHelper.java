package com.signetz.sso.helper;

import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;
import com.duangframework.sso.core.IAutoLoginHelper;

public class AutoLoginHelper implements IAutoLoginHelper {

    @Override
    public String doAutoLogin(String username, IRequest request) {
        // TODO 登录到业务系统
        return null;
    }

    @Override
    public void doLogout(IRequest request, IResponse response) {

    }
}
