package com.nike.parsebean.impl;

import com.nike.parsebean.OrmHandle;
import com.nike.util.OrmUtils;

import java.sql.ResultSet;

public class SingleBean<T> implements OrmHandle<T> {
    private Class<T> clazz;
    public SingleBean(Class<T> clazz){
        this.clazz = clazz;
    }
    @Override
    public T ormResultHandle(ResultSet set) throws Exception {
        T newInstast = null;
        if (set.next()){
             newInstast = OrmUtils.columnToProperies(set, clazz);
        }
        return newInstast;
    }
}
