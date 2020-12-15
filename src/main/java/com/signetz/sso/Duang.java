package com.signetz.sso;

import com.duangframework.mvc.http.enums.EnvEnum;
import com.duangframework.mvc.plugin.IPlugin;
import com.duangframework.mvc.plugin.PluginChain;
import com.duangframework.server.Application;
import com.duangframework.sso.handler.SSOHandler;
import com.duangframework.sso.plugins.SSOPulgin;

import java.util.List;

public class Duang {
    public static void main(String[] args) {
        Application.duang().port(8080)
                .plugins(new PluginChain() {
                    @Override
                    public void addPlugin(List<IPlugin> pluginList) throws Exception {
                        // 后台权限验证,dev模式下可以先注释
                        pluginList.add(new SSOPulgin(new SSOHandler()));
                    }
                })
                .env(EnvEnum.DEV)
                .cors(true)
                .run();
    }
}
