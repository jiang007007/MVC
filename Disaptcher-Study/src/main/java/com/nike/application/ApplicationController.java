package com.nike.application;

import com.nike.loadresouce.LoadPropertiesResource;
import com.nike.util.AnnMeta;
import java.util.Map;
public class ApplicationController {
    private static Map<String, AnnMeta>  controllerMap ;

    static {
            controllerMap= LoadPropertiesResource.scannerController();
    }

    public static AnnMeta getController(String key){
   //     AnnMeta annMeta = controllerMap.getOrDefault(key, null);
        return controllerMap.getOrDefault(key, null);
    }

}
