package com.nike.servlet;

import com.nike.application.ActionApplication;
import com.nike.application.ApplicationServletContextUtils;
import com.nike.bean.ServletContextBean;
import com.nike.util.AnnMeta;
import com.sun.xml.internal.ws.developer.EPRRecipe;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = "*.action")
public class DispatchersServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        ApplicationServletContextUtils contextUtils = new ApplicationServletContextUtils();
        contextUtils.getThreadLocal().set(new ServletContextBean(req,resp));

        String servletPath = req.getServletPath();
        String uri = servletPath.substring(0,servletPath.lastIndexOf("."));
        Map<String, AnnMeta> controllerMap= null;
        try {
            controllerMap = ActionApplication.scannerController("scannbases.properties");
        }catch (Exception e){
            System.err.println("读取配置文件失败");
        }

        System.out.println(uri);
    }

    private void setStats(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(404);
        PrintWriter writer = resp.getWriter();
        writer.println("找到不到指定["+req.getServletPath().substring(0,req.getServletPath().lastIndexOf("."))+"]的地址");
        writer.flush();
        writer.close();
    }
}
