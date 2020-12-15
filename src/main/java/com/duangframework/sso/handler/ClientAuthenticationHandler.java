package com.duangframework.sso.handler;

import com.duangframework.exception.MvcException;
import com.duangframework.kit.ClassKit;
import com.duangframework.kit.ObjectKit;
import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;
import com.duangframework.mvc.http.handler.IHandler;
import com.duangframework.sso.core.IAutoLoginHelper;
import com.duangframework.sso.core.SSOUserData;
import com.duangframework.sso.exceptions.SSOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

class ClientAuthenticationHandler implements IHandler {

	private static final Logger logger = LoggerFactory.getLogger(ClientAuthenticationHandler.class);

	private IAutoLoginHelper autoLoginHelper = null;

	protected ClientAuthenticationHandler(Properties properties) throws SSOException {
		String autoLoginHelperClassStr = properties.getProperty("auto.LoginHelper.class");
		if (ToolsKit.isNotEmpty(autoLoginHelperClassStr)) {
			autoLoginHelper = ObjectKit.newInstance(autoLoginHelperClassStr);
		}
	}

	public void doHandler(String target, IRequest request, IResponse response) throws SSOException {
		SSOUserData userData = SSOUserData.getInstance();
		if (userData.isUserChanged()) {
			String username = userData.getCurrentUsername();
			userData.acceptUserChange();
			if (username != null && username.length() > 0) {
				if (autoLoginHelper.doAutoLogin(username, request) == null) {
					throw new SSOException("根据用户名：" + username + "找不到相应的用户，跳转到无权限页面。"+autoLoginHelper.getClass().getName()+".doAutoLogin失败");
				}
				if (logger.isDebugEnabled()) {
					logger.debug("成功执行登录操作，登录用户：" + username);
				}
			} else {
				autoLoginHelper.doLogout(request, response);
				if (logger.isDebugEnabled()) {
					logger.debug("成功执行注销操作。");
				}
			}
		}
	}
}