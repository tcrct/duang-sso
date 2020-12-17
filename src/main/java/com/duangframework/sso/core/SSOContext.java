package com.duangframework.sso.core;

import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;

import java.util.HashMap;
import java.util.Map;

/**
 *  SSOContext
 *
 * @author Laotang
 * @since 1.0
 */
public class SSOContext {

    private static ThreadLocal instance = new ThreadLocal();
    private String currentUsername;
    private String originUsername;
    private Map<String,Object> parameter;
    private IRequest request;
    private IResponse response;
    private Map<String,Object> tmpParameter = new HashMap();

    public static SSOContext getInstance() {
        return (SSOContext)instance.get();
    }

    /**
     *  初始化到threadLocal
     * @param originUsername 原来的名字
     * @param parameter HttpSession map集合
     * @param request 请求对象
     * @param response 返回对象
     * @return
     */
    public static SSOContext initThreadLocal(String originUsername, Map parameter, IRequest request, IResponse response) {
        SSOContext context = new SSOContext(originUsername, parameter, request, response);
        instance.set(context);
        return context;
    }

    /**
     * 移除
     */
    public static void removeThreadLocal() {
        instance.remove();
    }

    protected SSOContext(String originUsername, Map parameter, IRequest request, IResponse response) {
        this.currentUsername = originUsername;
        this.originUsername = originUsername;
        this.parameter = parameter;
        this.request = request;
        this.response = response;
    }

    public String getCurrentUsername() {
        return this.currentUsername;
    }

    public String getOriginUsername() {
        return this.originUsername;
    }

    public Object getParameter(String key) {
        return this.parameter.get(key);
    }

    public IRequest getRequest() {
        return this.request;
    }

    public IResponse getResponse() {
        return this.response;
    }

    public Object getTmpParameter(String key) {
        return this.tmpParameter.get(key);
    }

    public boolean isUserChanged() {
        return !this.originUsername.equalsIgnoreCase(this.currentUsername);
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public void setOriginUsername(String originUsername) {
        this.originUsername = originUsername;
    }

    public void setParameter(String key, Object value) {
        this.parameter.put(key, value);
    }

    public void setTmpParameter(String key, Object value) {
        this.tmpParameter.put(key, value);
    }

}
