package com.nike.loadresouce;

import com.nike.annotation.Controller;
import com.nike.annotation.ReqestMapping;
import com.nike.application.ApplicationServletContextUtils;
import com.nike.util.AnnMeta;
import com.sun.org.apache.bcel.internal.generic.RET;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class LoadPropertiesResource {

    private static final Properties PROPERTIES= new Properties();
    private static final String GLOBAL_RESOURCE="scannbases.properties";
    private static final String PATH=File.separator;
    private static final String WEN_INT=PATH +"WEB-INF"+PATH+ "classes";

    static {
        try {
        InputStream url = LoadPropertiesResource.class.getClassLoader().getResourceAsStream(GLOBAL_RESOURCE);
            PROPERTIES.load(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  加载 自动文件 扫描是否有自定的注解
     * @return
     */
    public static Map<String,AnnMeta> scannerController() {
        try {
            Set<Map.Entry<Object, Object>> entrys = PROPERTIES.entrySet();
            Map<String,AnnMeta> annotaionMap  = Collections.synchronizedMap(new HashMap<>());;
            for (Map.Entry<Object, Object> entry: entrys){

                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                //扫描包
                if (key.startsWith("basepackage")){
                    String resource ="";
                            //Objects.requireNonNull(URLClassLoader.getSystemClassLoader().getResource(GLOBAL_RESOURCE)).getFile();
                    ServletContext context = ApplicationServletContextUtils.getThreadLocal().get().getContext();
                    resource=context.getRealPath(WEN_INT+ PATH +GLOBAL_RESOURCE);
                    // D:\apache-tomcat-8.5.47\webapps\DisaptcherStudy\WEB-INF\classes\scannbases.properties
                    resource= resource.substring(0,resource.lastIndexOf(PATH)+1);
                    resource = resource +value.replace(".",File.separator);
                    File[] files = new File(resource).listFiles();
                    for (File file : files)
                    {
                            loadBeanClass(file,value,annotaionMap);
                    }
                }else{
                    break;
                }
            }
            return annotaionMap;
        } catch (ClassNotFoundException clazz){
                System.err.println("没有在[" +GLOBAL_RESOURCE+"]的GLOBAL_RESOURCE中指扫描类路径");
        }

        return null;
    }
    private  static void findBeanClass(String clazzStr, Map<String,AnnMeta> annotaionMap) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(clazzStr);
        Controller[] clazzAnntataion = aClass.getAnnotationsByType(Controller.class);
        if (clazzAnntataion.length>0){
            ReqestMapping[] declaredAnnotationsByType = aClass.getDeclaredAnnotationsByType(ReqestMapping.class);
            String mapping="";
            for (ReqestMapping es: declaredAnnotationsByType){
                mapping = es.value();
            }
            Method[] methods = aClass.getMethods();
            AnnMeta annMeta = new AnnMeta();
            String value=null;
            for (Method method: methods){
                ReqestMapping[] methodAnnotationsByType = method.getAnnotationsByType(ReqestMapping.class);
                for (ReqestMapping es:methodAnnotationsByType){
                    value =mapping+ es.value();//RequestMapping定义的路由
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
    private  static  void loadBeanClass(File  file,String value, Map<String,AnnMeta> annotaionMap) throws ClassNotFoundException {
        File[] files = file.listFiles();
        for(File fi: files){
            if (fi.getName().endsWith(".class")){
                findBeanClass(value+"."+file.getName().substring(0,file.getName().lastIndexOf(".")), annotaionMap);
            }else {
                loadBeanClass(fi,value,annotaionMap);
            }
        }
    }


    //加载po
    public static List<Class<?>> getLoadbeanDinfintion(){
        ArrayList<Class<?>> laodBeanClass = new ArrayList<>();
        Set<Map.Entry<Object, Object>> entries = PROPERTIES.entrySet();
        for (Map.Entry<Object,Object> entry: entries){
            String key =(String)entry.getKey();
            String value =(String)entry.getValue();
            if (key.endsWith("initbean")){
                String resource ="";
                //Objects.requireNonNull(URLClassLoader.getSystemClassLoader().getResource(GLOBAL_RESOURCE)).getFile();
                ServletContext context = ApplicationServletContextUtils.getThreadLocal().get().getContext();
                resource=context.getRealPath(WEN_INT+ PATH +GLOBAL_RESOURCE);
                // D:\apache-tomcat-8.5.47\webapps\DisaptcherStudy\WEB-INF\classes\scannbases.properties
                resource= resource.substring(0,resource.lastIndexOf(PATH)+1);
                resource = resource +value.replace(".",File.separator);
                File file = new File(resource);
                loadBeanDinfintion(file,laodBeanClass);
            }
        }

        return  laodBeanClass;
    }

    private static void  loadBeanDinfintion(File file, ArrayList<Class<?>> list){
        File[] files = file.listFiles();
        for (File f: files){
            if (f.getName().endsWith(".class")){
                list.add(f.getName().getClass());
            }else {
                loadBeanDinfintion(f,list);
            }
        }
    }
}
