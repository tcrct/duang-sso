package com.duangframework.sso.core;

import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;

public interface IAutoLoginHelper {

    String doAutoLogin(String username, IRequest request);

    public void doLogout(IRequest request, IResponse response);
}
