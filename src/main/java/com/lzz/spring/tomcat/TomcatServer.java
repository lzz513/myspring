package com.lzz.spring.tomcat;

import com.lzz.spring.mvc.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {

    private Tomcat tomcat;

    public void startServer() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.start();

        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        DispatcherServlet servlet = new DispatcherServlet();
        Tomcat.addServlet(context, "dispatcherServlet", servlet)
                .setAsyncSupported(true);

        context.addServletMappingDecoded("/", "dispatcherServlet");

        tomcat.getHost().addChild(context);

        Thread tomcatAwaitThread = new Thread("tomcat_await_thread") {
            @Override
            public void run() {

                TomcatServer.this.tomcat.getServer().await();
            }
        };

        tomcatAwaitThread.setDaemon(false);
        tomcatAwaitThread.start();
    }


}

