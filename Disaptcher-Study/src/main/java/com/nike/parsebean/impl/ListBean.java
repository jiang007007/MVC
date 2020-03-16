package com.nike.parsebean.impl;

import com.nike.parsebean.OrmHandle;
import com.nike.util.OrmUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ListBean<T> implements OrmHandle<List<T>> {
    private Class<T> clazz;
    public ListBean(Class<T> clazz){
        this.clazz = clazz;
    }
    @Override
    public List<T> ormResultHandle(ResultSet set) throws Exception {
          if (set == null){
              return  new ArrayList<>();
          }
        ArrayList<T> arrayList = new ArrayList<>();
        while (set.next()){
              T t = OrmUtils.columnToProperies(set, clazz);
            arrayList.add(t);
          }
        return arrayList;
    }
}
