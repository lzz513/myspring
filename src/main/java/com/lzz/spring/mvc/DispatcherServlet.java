package com.lzz.spring.mvc;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet implements Servlet {


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        String uri = ((HttpServletRequest)servletRequest).getRequestURI();
        System.out.println(uri);
        try {
            MapperHandler mapperHandler = HandlerManger.requestMapper.get(uri);
            if (mapperHandler != null)
                mapperHandler.handler((HttpServletRequest)servletRequest, (HttpServletResponse) servletResponse);
            else {
                ((HttpServletResponse)servletResponse).getWriter().write("falie");
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
