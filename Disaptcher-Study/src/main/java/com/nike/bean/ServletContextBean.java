package com.nike.bean;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 封装与对象 做参数反射使用
 */
public class ServletContextBean {
        private HttpServletRequest request;
        private HttpServletResponse response;
        private HttpSession httpSession;
        private ServletContext context;

    public ServletContextBean(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.httpSession = request.getSession();
        this.context = request.getServletContext();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public ServletContext getContext() {
        return context;
    }
}
