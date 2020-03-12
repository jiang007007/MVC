package com.nike.application;

import com.nike.annotation.Controller;
import com.nike.annotation.ReqestMapping;
import com.nike.util.AnnMeta;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.*;

public class ActionApplication {

    private static final Properties PROPERTIES= new Properties();
    private static Map<String,AnnMeta> annotaionMap = Collections.synchronizedMap(new HashMap<>());

    /**
     *  加载 自动文件 扫描是否有自定的注解
     * @param location
     * @return
     */
    public static <T> T scannerController(String location) throws Exception {
    loadFile(location);

        Set<Map.Entry<Object, Object>> entrys = PROPERTIES.entrySet();
        for (Map.Entry<Object, Object> entry: entrys){
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.startsWith("basepackage")){
                String resource = URLClassLoader.getSystemClassLoader().getResource(location).getFile();
                ///E:/git/MVC/Disaptcher-Study/target/classes/scannbases.properties
                resource= resource.substring(1,resource.lastIndexOf("/")+1).replace("/",File.separator);
                resource = resource +value.replace(".",File.separator);
                File[] files = new File(resource).listFiles();
                for (File file : files)
                {
                    if (file.getName().endsWith(".class")){
                        findBeanClass(value+"."+file.getName().substring(0,file.getName().lastIndexOf(".")));
                    }
                }
            }else{
                break;
            }
        }
        return (T) annotaionMap;
    }
    private  static void findBeanClass(String clazzStr) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(clazzStr);
        Controller[] clazzAnntataion = aClass.getAnnotationsByType(Controller.class);
        if (clazzAnntataion.length>0){
            Method[] methods = aClass.getMethods();
            AnnMeta annMeta = new AnnMeta();
            String value=null;
            for (Method method: methods){
                ReqestMapping[] methodAnnotationsByType = method.getAnnotationsByType(ReqestMapping.class);
                for (ReqestMapping es:methodAnnotationsByType){
                    value = es.value();//RequestMapping定义的路由
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    annMeta.setMethod(method);
                    annMeta.setParameters(parameterTypes);
                    annMeta.setClazz(aClass);
                }
            }
            annotaionMap.put(value,annMeta);
        }
    }

    //递归从查找
    private  static  void loadBeanClass(File  file){
        File[] files = file.listFiles();
        for(File fi: files){

        }
    }
    //读取配置文件
    public   static void loadFile(String path) throws Exception {
        InputStream url = URLClassLoader.getSystemResourceAsStream(path);
        PROPERTIES.load(url);

    }

}
