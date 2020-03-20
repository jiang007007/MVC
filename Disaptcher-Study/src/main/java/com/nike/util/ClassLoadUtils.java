package com.nike.util;

import com.nike.annotation.Controller;
import com.nike.annotation.ReqestMapping;
import com.nike.application.ApplicationController;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取类加载器ClassLoader
 * 获取类
 * 加载类
 */
public class ClassLoadUtils {
    /**
     *
     * @return 返回的时当前线程的系统类加载器 or bootstrap类加载器
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * @return 所需类对象
     */
    public static Class<?> loadClass(String clazz){
        return  loadClass(clazz,true);
    }

    /**
     *
     * @param clazz  将要初始化类的全限名称
     * @param isInitialized  true  class将被初始化
     * @return 所需类对象
     */

    public static Class<?> loadClass(String clazz,boolean isInitialized){
        Class<?> aClass= null;
        try {
           aClass = Class.forName(clazz, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return aClass;
    }

    /**
     *  加载指定包下的类
     * @param basePackage  指定的包
     * @return  返回指定包下的class 对象
     */
    public static Set<Class<?>> getClassSet(String basePackage){
        Set<Class<?>> classSet = new HashSet<>();
        try {
            basePackage = basePackage.replace(".", File.separator);
            Enumeration<URL> resources = getClassLoader().getResources(basePackage);
            while (resources.hasMoreElements()){
                URL url = resources.nextElement();
                //环境 ftp, http, nntp
                String protocol = url.getProtocol();
                System.out.println(protocol);
                if (protocol.equalsIgnoreCase("file")){
                    String packagePath = url.getPath();
//                    /E:/git/MVC/Disaptcher-Study/target/classes/com%5cnike%5cpo
                    String marth = packagePath.substring(packagePath.indexOf("com")+ 3,packagePath.indexOf("nike"));
                    packagePath = packagePath.replace(marth,File.separator);
                    findClass(classSet,packagePath,basePackage);
                }
            }
            return classSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //查找指定包下的class文件按然后初始化
    private static void findClass(Set<Class<?>> classSet,String packagePath,String packageName){
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        for (File file: files){
            String fileName = file.getName();
            if (file.isDirectory()){
                //递归
                String subPackagePath =fileName;
                subPackagePath = packagePath+ File.separator +subPackagePath;
                String subPackageName = packageName + "." + subPackagePath;
                findClass(classSet, subPackagePath, subPackageName);
            }else {
                //.class文件
               String  clazzName =fileName.substring(0,fileName.lastIndexOf("."));
                clazzName=packageName + File.separator + clazzName;
                //com.nike.po.xx;
                addClass(classSet,clazzName);
            }
        }

    }

    private static void addClass(Set<Class<?>> classSet,String className){
        className =className.replace(File.separator,".");
        Class<?> cls = loadClass(className);
        if (!isByAnnotation(cls)){
            classSet.add(cls);
        }
    }

    //判断是否有指定的注解
    private static boolean isByAnnotation(Class<?> clzz){
        boolean flags =false;
        Controller[] clazzAnntataion = clzz.getAnnotationsByType(Controller.class);
        if (clazzAnntataion.length>0){
            flags = true;
            ReqestMapping[] declaredAnnotationsByType = clzz.getDeclaredAnnotationsByType(ReqestMapping.class);
            String mapping="";
            for (ReqestMapping es: declaredAnnotationsByType){
                mapping = es.value();
            }
            Method[] methods = clzz.getMethods();
            AnnMeta annMeta = new AnnMeta();
            String value=null;
            for (Method method: methods){
                ReqestMapping[] methodAnnotationsByType = method.getAnnotationsByType(ReqestMapping.class);
                for (ReqestMapping es:methodAnnotationsByType){
                    value =mapping+ es.value();//RequestMapping定义的路由
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    annMeta.setMethod(method);
                    annMeta.setParameters(parameterTypes);
                    annMeta.setClazz(clzz);
                }
            }
            ApplicationController.controllerMap.put(value,annMeta);
        }
        return flags;
    }
}
