package com.nike.application;

import com.nike.loadresouce.LoadPropertiesResource;
import com.nike.util.AnnMeta;

import java.util.List;
import java.util.Map;
public class ApplicationController {
    private static Map<String, AnnMeta>  controllerMap ;
    private static List<Class<?>> BeanDefinition;
    static {
            controllerMap= LoadPropertiesResource.scannerController();
            BeanDefinition = LoadPropertiesResource.getLoadbeanDinfintion();
    }

    public static AnnMeta getController(String key){
   //     AnnMeta annMeta = controllerMap.getOrDefault(key, null);
        return controllerMap.getOrDefault(key, null);
    }


    //获取BeanDinfintion
    public static List<Class<?>> getBeanDeifnition(){
        return  BeanDefinition;
    }
}
