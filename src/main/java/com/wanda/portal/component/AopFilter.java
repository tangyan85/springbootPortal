package com.wanda.portal.component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Aop过滤器
 *
 * @author cloud He
 * @since 1.0.0 2018/7/16
 */
public class AopFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        AopServletContext.setRequest(request);
        AopServletContext.setResponse(response);
//        Object userObject = AopServletContext.getRequest().getSession().getAttribute("user");
//        if (userObject == null) {
//            request.getRequestDispatcher("/index").forward(request, response);
//        } else {
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
