package com.duangframework.sso.plugins;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.core.helper.HandlerHelper;
import com.duangframework.mvc.http.handler.IHandler;
import com.duangframework.mvc.plugin.IPlugin;

public class SSOPulgin implements IPlugin {

    private IHandler ssoHandler;
    private Integer index = 0; //默认在第一位

    public SSOPulgin(IHandler ssoHandler) {
        this.ssoHandler = ssoHandler;
    }

    public SSOPulgin(Integer handlerIndex, IHandler ssoHandler) {
        this.index = handlerIndex;
        this.ssoHandler = ssoHandler;
    }

    @Override
    public void start() throws Exception {
        // 如果不为null
        if(ToolsKit.isNotEmpty(ssoHandler)) {
            // 添加到第一位
            HandlerHelper.getBeforeHandlerList().add(index, ssoHandler);
        }
    }

    @Override
    public void stop() throws Exception {
        ssoHandler = null;
    }
}
