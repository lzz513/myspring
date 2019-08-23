package com.lzz.spring.scan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassScanner {


    String packageName = null;
    ClassLoader classLoader = ClassScanner.class.getClassLoader();
    List<Class<?>> classList = new ArrayList<>();

    public List<Class<?>> getClassList(String packageName1) {
        parseResourse(packageName1);
        return classList;
    }

    private void parseResourse(final String packageName) {
        URI uri = null;
        try {
            uri = classLoader.getResource(packageName.replace(".", "/")).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File file = new File(uri);
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()){
                    parseResourse(packageName+"/"+f.getName());
                    return true;
                }
                if (f.getName().endsWith(".class")){
                    Class<?> clazz = null;
                    try {
                        String path = packageName.replace("/", ".")+"."+f.getName().replace(".class", "");
                        clazz = classLoader.loadClass(path);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (clazz != null){
                        classList.add(clazz);
                    }
                }
                return false;
            }
        });

    }
}
