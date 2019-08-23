package com.lzz.spring.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class MapperHandler {

    List<String> params;
    Object bean;
    Method method;

    public MapperHandler(Object bean, Method method, List<String> params) {
        this.bean = bean;
        this.method = method;
        this.params = params;
    }

    public void handler(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        Object args[] = new Object[params.size()];
        int i = 0;
        for (String parameter:params){
            System.out.println(parameter);
            if (request.getParameter(parameter) != null){
                args[i++] = request.getParameter(parameter);
                System.out.println(parameter);
            }
        }
        Object res = null;
        res = method.invoke(bean, args);
        response.getWriter().write(res.toString());
    }

}
