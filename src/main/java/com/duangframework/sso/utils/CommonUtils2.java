package com.duangframework.sso.utils;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.http.IRequest;
import com.duangframework.mvc.http.IResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils2.class);

    public static String safeGetParameter(IRequest request, String parameter) {
        if ("POST".equalsIgnoreCase(request.getMethod()) && "logoutRequest".equalsIgnoreCase(parameter)) {
            LOGGER.warn("safeGetParameter called on a POST HttpServletRequest for LogoutRequest.  Cannot complete check safely.  Reverting to standard behavior for this Parameter");
            return request.getParameter(parameter);
        } else {
            return request.getQueryString() != null && request.getQueryString().indexOf(parameter) != -1 ? request.getParameter(parameter) : null;
        }
    }

    public static String constructServiceUrl(IRequest request, IResponse response, String service, String serverName, String artifactParameterName, boolean encode) {
        if (ToolsKit.isNotEmpty(service)) {
            return encode ? response.encodeURL(service) : service;
        } else {
            StringBuffer buffer = new StringBuffer();
            synchronized(buffer) {
                if (!serverName.startsWith("https://") && !serverName.startsWith("http://")) {
                    buffer.append(request.isSSL() ? "https://" : "http://");
                }

                buffer.append(serverName);
                buffer.append(request.getRequestURI());
                if (ToolsKit.isNotEmpty(request.getQueryString())) {
                    int location = request.getQueryString().indexOf(artifactParameterName + "=");
                    if (location == 0) {
                        String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("serviceUrl generated: " + returnValue);
                        }

                        return returnValue;
                    }

                    buffer.append("?");
                    if (location == -1) {
                        buffer.append(request.getQueryString());
                    } else if (location > 0) {
                        int actualLocation = request.getQueryString().indexOf("&" + artifactParameterName + "=");
                        if (actualLocation == -1) {
                            buffer.append(request.getQueryString());
                        } else if (actualLocation > 0) {
                            buffer.append(request.getQueryString().substring(0, actualLocation));
                        }
                    }
                }
            }

            String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("serviceUrl generated: " + returnValue);
            }

            return returnValue;
        }
    }


    public static boolean checkPath(IRequest request, String path) {
        if (ToolsKit.isEmpty(path)) {
            return false;
        } else {
            String requestURL = request.getRequestURI();
            if (ToolsKit.isNotEmpty(request.getContextPath())) {
                requestURL = requestURL.substring(request.getContextPath().length());
            }

            String[] paths = path.trim().split("\\s*;\\s*");

            for(int i = 0; i < paths.length; ++i) {
                String pathURL = paths[i];
                int index = pathURL.indexOf(63);
                if (index == -1) {
                    if (requestURL.startsWith(pathURL)) {
                        return true;
                    }
                } else {
                    String pathQuery = pathURL.substring(index + 1);
                    pathURL = pathURL.substring(0, index);
                    boolean match = true;
                    if (requestURL.equals(pathURL)) {
                        String[] queryParams = pathQuery.split("&");

                        for(int j = 0; j < queryParams.length; ++j) {
                            String queryParam = queryParams[j];
                            index = queryParam.indexOf(61);
                            if (index != -1) {
                                String value = request.getParameter(queryParam.substring(0, index));
                                if (!queryParam.substring(index + 1).equals(value)) {
                                    match = false;
                                    break;
                                }
                            }
                        }

                        if (match) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }

}
