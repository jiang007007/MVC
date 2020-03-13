package com.nike.application;

import com.nike.bean.ServletContextBean;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 每个请求线程保存一份与对象
 *  ThreadLocal
 */
public class ApplicationServletContextUtils {
    private static final ThreadLocal<ServletContextBean> THREAD_LOCAL = new ThreadLocal<>();


    public static ThreadLocal<ServletContextBean> getThreadLocal(){
        return  THREAD_LOCAL;
    }
    public static HttpServletRequest getRequest(){
        ServletContextBean servletContextBean = THREAD_LOCAL.get();
        return servletContextBean.getRequest();
    }

    public static HttpServletResponse getResponse(){
        ServletContextBean servletContextBean = THREAD_LOCAL.get();
        return servletContextBean.getResponse();
    }


    public static HttpSession getSession(){
        ServletContextBean servletContextBean = THREAD_LOCAL.get();
        return servletContextBean.getHttpSession();
    }

    public static ServletContext getContext(){
        ServletContextBean servletContextBean = THREAD_LOCAL.get();
        return servletContextBean.getContext();
    }
}
