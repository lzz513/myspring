package com.lzz.spring.core;

import com.lzz.spring.aop.*;
import com.lzz.spring.db.Insert;
import com.lzz.spring.db.Mapper;
import com.lzz.spring.db.Param;
import com.lzz.spring.db.Select;
import com.lzz.spring.exception.BeanWiredException;
import com.lzz.spring.mvc.Component;
import com.lzz.spring.mvc.Controller;
import com.lzz.spring.utils.JdbcUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {


    public static Map<Class<?>, Object> beans = new ConcurrentHashMap<>();
    public static Set<Class<?>> autowroedClassSet =
            Collections.synchronizedSet(new HashSet<Class<?>>());

    public static void initBean(List<Class<?>> classList) throws Exception {
        List<Class<?>> rmList = new ArrayList<>();
        Set<Class<?>> aopClassSet = new HashSet<>();

        while (classList.size() != 0) {
            int beforeNumber = classList.size();
            for (Class<?> clazz:classList) {
                if (clazz.isAnnotationPresent(Aspect.class)){
                    aopClassSet.add(clazz);
                }
                if (finishBean(clazz)){
                    rmList.add(clazz);
                }

            }
            classList.removeAll(rmList);
            if (beforeNumber == classList.size()){
                throw new BeanWiredException("bean nest");
            }
        }
        for (Class<?> cls:aopClassSet){
            resolveAopBean(cls);
        }
        for (Class<?> cls:autowroedClassSet){
            if (!finishBean(cls)) {
                throw new BeanWiredException("proxy wired faile");
            }
        }
    }

    private static void resolveAopBean(Class<?> cls) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Method before = null;
        Method after = null;
        Object target = null;
        String targetMethod= null;
        for (Method method:cls.getDeclaredMethods()){
            if (method.isAnnotationPresent(Before.class)){
                before = method;
            }
            else if (method.isAnnotationPresent(After.class)){
                after = method;
            }
            else if (method.isAnnotationPresent(PointCut.class)){
                String classPath = method.getAnnotation(PointCut.class).value();
                target = Thread.currentThread().getContextClassLoader()
                        .loadClass(classPath.substring(0, classPath.lastIndexOf("."))).newInstance();
                targetMethod = classPath.substring(classPath.lastIndexOf(".")+1);
            }
        }
        Object proxyBean = new ProxyDyna().createProxybean(before, after, target, cls.newInstance(), targetMethod);
        BeanFactory.beans.put(target.getClass(), proxyBean);
    }

    private static boolean finishBean(Class<?> cls) throws IllegalAccessException, InstantiationException {

        if (cls.isAnnotationPresent(Mapper.class)){
            return finishDaoBean(cls);
        }

        if (!cls.isAnnotationPresent(Controller.class) &&
        !cls.isAnnotationPresent(Component.class))
            return true;

        Object bean = cls.newInstance();
        for (Field field:cls.getDeclaredFields()){
            if (field.isAnnotationPresent(AutoWired.class)){
                autowroedClassSet.add(cls);
                Class<?> fieldType = field.getType();
                Object fieldValue = BeanFactory.beans.get(fieldType);
                if (fieldValue == null){
                    for (Class<?> clazz:BeanFactory.beans.keySet()){
                        if (fieldType.isAssignableFrom(clazz)){
                            fieldValue = BeanFactory.beans.get(clazz);
                            break;
                        }
                    }
                }
                if (fieldValue == null)
                    return false;
                field.setAccessible(true);
                field.set(bean, fieldValue);
            }
        }
        BeanFactory.beans.put(cls, bean);
        return true;
    }

    private static boolean finishDaoBean(Class<?> cls) {
        Object proxyBean = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{cls}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object res = null;

                if (method.isAnnotationPresent(Insert.class)){
                    String sql = method.getAnnotation(Insert.class).value();
                    List<String> paramNameList = new ArrayList<>();
                    sql = JdbcUtils.getParamNameListAndReplace(paramNameList, sql);
                    JdbcUtils.insert(sql, getRealArgs(method, args, paramNameList));
                }

                else if (method.isAnnotationPresent(Select.class)){
                    String sql = method.getAnnotation(Select.class).value();
                    List<String> paramNameList = new ArrayList<>();
                    sql = JdbcUtils.getParamNameListAndReplace(paramNameList, sql);
                    System.out.println(method.getReturnType().getName());
                    res = JdbcUtils.queryAll(sql, getRealArgs(method, args, paramNameList),
                            getFangXingClass(method));
                }


                return res;
            }

            private Class<?> getFangXingClass(Method method) throws ClassNotFoundException {
                Type genericReturnType = method.getGenericReturnType();
                System.out.println(genericReturnType.getTypeName());
                //获取返回值的泛型参数
                String name =  ((ParameterizedType)genericReturnType).getActualTypeArguments()[0].getTypeName();
                return Class.forName(name);
            }

            private Object[] getRealArgs(Method method, Object args[], List<String> paramNameList){
                int i = 0;
                HashMap<String, Object> paramMapper = new HashMap();
                for (Parameter parameter:method.getParameters()){
                    if (parameter.isAnnotationPresent(Param.class)){
                        String name = parameter.getAnnotation(Param.class).value();
                        paramMapper.put(name, args[i++]);
                        System.out.println(name);
                    }
                    else {
                        parseBeanMapper(paramMapper, parameter.getType(), args[i++]);
                    }
                }
                Object realArgs[] = new Object[paramNameList.size()];
                for (int j = 0; j < paramNameList.size(); j++){
                    realArgs[j] = paramMapper.get(paramNameList.get(j));
                }
                return realArgs;
            }

            private void parseBeanMapper(HashMap<String, Object> paramMapper, Class<?> type, Object arg) {
                for (Field field:type.getDeclaredFields()){
                    try {
                        field.setAccessible(true);
                        paramMapper.put(field.getName(), field.get(arg));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        beans.put(cls, proxyBean);
        return true;
    }
}
