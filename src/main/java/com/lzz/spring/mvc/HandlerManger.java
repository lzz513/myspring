package com.lzz.spring.mvc;

import com.lzz.spring.core.BeanFactory;
import com.lzz.spring.db.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class HandlerManger {

    public static Map<String, MapperHandler> requestMapper = new HashMap<>();

    public static void resolveMappingHandler(List<Class<?>> classList){
        for (Class cls:classList){
            if (cls.isAnnotationPresent(Controller.class)){
                parseHandleFromController(cls);
            }
        }
    }

    private static void parseHandleFromController(Class cls) {
        String pre = "";
        if (cls.isAnnotationPresent(RequestMapping.class)){
            RequestMapping requestMapping = (RequestMapping) cls.getDeclaredAnnotation(RequestMapping.class);
            pre = requestMapping.value();
        }
        if (!"".equals(pre) && pre.charAt(0) != '/'){
            pre = "/"+pre;
        }
        Object bean = BeanFactory.beans.get(cls);
        for (Method method:cls.getMethods()){
            if (method.isAnnotationPresent(RequestMapping.class)){
                String path = method.getAnnotation(RequestMapping.class).value();
                path.replace("/", "");
                path = pre+"/"+path;
                List<String> params = new ArrayList<>();
                for (Parameter parameter : method.getParameters()) {
                    params.add(parameter.getAnnotation(Param.class).value());
                }
                MapperHandler mapperHandler = new MapperHandler(bean, method, params);
                requestMapper.put(path, mapperHandler);
                System.out.println(path);
            }
        }
    }

}
