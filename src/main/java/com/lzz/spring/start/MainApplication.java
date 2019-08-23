package com.lzz.spring.start;

import com.lzz.spring.core.BeanFactory;
import com.lzz.spring.mvc.HandlerManger;
import com.lzz.spring.scan.ClassScanner;
import com.lzz.spring.tomcat.TomcatServer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainApplication {

    public static void run(Class<?> clazz) throws Exception {
        TomcatServer server = new TomcatServer();
        server.startServer();
        ClassScanner sc = new ClassScanner();
        List<Class<?>> classList = sc.getClassList(clazz.getPackage().getName());
        List<Class<?>> classList1 = new ArrayList<>(classList);
        BeanFactory.initBean(classList1);
        HandlerManger.resolveMappingHandler(classList);
    }




}

