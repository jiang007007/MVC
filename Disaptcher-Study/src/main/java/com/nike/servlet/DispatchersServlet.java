package com.nike.servlet;

import com.nike.application.ApplicationController;
import com.nike.application.ApplicationServletContextUtils;
import com.nike.bean.ServletContextBean;
import com.nike.util.AnnMeta;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@WebServlet(urlPatterns = "*.action")
public class DispatchersServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        ApplicationServletContextUtils.getThreadLocal().set(new ServletContextBean(req,resp));

        String servletPath = req.getServletPath();
        String uri = servletPath.substring(0,servletPath.lastIndexOf("."));
        AnnMeta annMeta = ApplicationController.getController(uri);
       try {
           if (annMeta != null){
               Class<?> currentClazz = annMeta.getClazz();
               Method currentMethod = annMeta.getMethod();
               //实例化对象
               Object Instance = currentClazz.newInstance();
               //参数类型
               Class<?>[] currentparameters = annMeta.getParameters();
               Object[] args = new Object[currentMethod.getParameterCount()];
               genarateMethodArg(currentparameters,args);
               Object invoke = currentMethod.invoke(Instance, args);
                if (invoke != null){
                    //逻辑处理
                }
           }
           return;
       }catch (Exception e){
           setStats(req, resp);
       }


        System.out.println(uri);
    }

    private void setStats(HttpServletRequest req, HttpServletResponse resp) {
            try {
                resp.setStatus(404);
                PrintWriter writer = resp.getWriter();
                writer.println("找到不到指定["+req.getServletPath().substring(0,req.getServletPath().lastIndexOf("."))+"]的地址");
                writer.flush();
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    /**
     *     根据获取到的方法参数类型注入值
     * @param parameterTypes
     * @param args
     */
    private void genarateMethodArg(Class<?>[] parameterTypes,Object ...args ) {
        if (args.length >0){
            int i=0;
            for (Class<?> clazz: parameterTypes){
                if (clazz ==HttpServletRequest.class){
                    args[i] =     ApplicationServletContextUtils.getRequest();
                }else if (clazz == HttpServletResponse.class){
                    args[i] = ApplicationServletContextUtils.getResponse();
                } else{
                    //实际需要注入的对象
                    args[i]= createBean(clazz);
                }

                i++;
            }
        }
    }
    //参数类型
    private <T> T createBean(Class<?> clazz)  {
       try {
           HttpServletRequest request = ApplicationServletContextUtils.getRequest();
           Field[] declaredField = clazz.getDeclaredFields();
           Object Instance = clazz.newInstance();
           for (Field field: declaredField){
               field.setAccessible(true);
               field.set(Instance,request.getParameter(field.getName()));
           }
           return (T) Instance;
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }
}
