package com.nike.loadresouce;

import com.nike.util.ClassLoadUtils;
import sun.misc.ClassLoaderUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LoadPropertiesResource {

    private static final Properties PROPERTIES= new Properties();
    private static final String GLOBAL_RESOURCE="scannbases.properties";

    static {
        try {
        InputStream url = ClassLoadUtils.getClassLoader().getResourceAsStream(GLOBAL_RESOURCE);
            PROPERTIES.load(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //加载po
    public static Set<Class<?>> getLoadbeanDinfintion(){
       Set<Class<?>> laodBeanClass =new HashSet<>();
        Set<Map.Entry<Object, Object>> entries = PROPERTIES.entrySet();
        for (Map.Entry<Object,Object> entry: entries){
            String value =(String)entry.getValue();
                laodBeanClass.addAll(Objects.requireNonNull(ClassLoadUtils.getClassSet(value)));
        }
        return laodBeanClass;
    }


}
