package com.wanda.portal.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Aop容器上下文
 *
 * @author cloud He
 * @since 1.0.0 2018/7/16
 */
public class AopServletContext {
    private static ThreadLocal<HttpServletRequest> httpRequest = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> httpResponse = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        return httpRequest.get();
    }

    public static void setRequest(HttpServletRequest httpRequest) {
        AopServletContext.httpRequest.set(httpRequest);
    }

    public static HttpServletResponse getResponse() {
        return httpResponse.get();
    }

    public static void setResponse(HttpServletResponse response) {
        httpResponse.set(response);
    }

    public static HttpSession getSession() {
        return httpRequest.get().getSession();
    }
} 
