package com.nike.util;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnMeta {
    //前端控制类
    private Class<?> clazz;
    //参数类型
    private Class<?> [] parameters;
    //方法
    private Method method;


    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public void setParameters(Class<?>[] parameters) {
        this.parameters = parameters;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "AnnMeta{" +
                "clazz=" + clazz +
                ", parameters=" + Arrays.toString(parameters) +
                ", method=" + method +
                '}';
    }
}
