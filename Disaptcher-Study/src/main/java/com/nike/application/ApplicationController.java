package com.nike.application;

import com.nike.loadresouce.LoadPropertiesResource;
import com.nike.util.AnnMeta;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationController {
    public static Map<String, AnnMeta>  controllerMap = new ConcurrentHashMap<>();
    private static Set<Class<?>> BeanDefinition;
    static {
            BeanDefinition = LoadPropertiesResource.getLoadbeanDinfintion();
    }

    public static AnnMeta getController(String key){
        return controllerMap.getOrDefault(key, null);
    }


    //获取BeanDinfintion
    public static Set<Class<?>> getBeanDeifnition(){
        return  BeanDefinition;
    }
}
