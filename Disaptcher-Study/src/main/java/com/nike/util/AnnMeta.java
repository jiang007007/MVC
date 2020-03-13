package com.nike.util;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnMeta {

    private Class<?> clazz;
    private Class<?> [] parameters;
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
