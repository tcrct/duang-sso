package com.duangframework.sso.common;

public class Const {

    /**
     * 用户数据KEY
     */
    public static final String SSO_USER_DATA_FIELD = "SSOUserData";

    /**
     *  SSO 配置文件名
     */
    public static final String FILTER_CONFIG_FILE = "sso-config.properties";


    /**
     *
     */
    public static final String SSO_USER_NAME_FIELD = "SSOClient.UserName";

    /**
     * cas服务器地址
     */
    public static final String CAS_SERVER_URL = "cas.serverUrl";

    /**
     * cas服务器验证令牌名称
     */
    public static final String CAS_TICKET = "cas.ticket";

    /**
     *跳转URL
     */
    public static final String LOGIN_URL = "login.Url";

    /**
     *不进行跳转的URL
     */
    public static final String NO_REDIRECT_URLS = "noRedirectUrls";

    /**
     * URL的编码格式
     */
    public static final String URL_CHARSET = "login.UrlCharset";

    /**
     * sso 登录验证完成后，透传到登录接口的参数名
     */
    public static final String LOGIN_PARAM_NAME = "login.param.name";

    public static String SSO_USERNAME = "SSO_UserName";

    public static final String ACCESS_KEY = "duang_sso_access_key";
    public static final String REDIRECT_URL = "duang_sso_redirect_url";
    public static String AK = "nobody";
}
