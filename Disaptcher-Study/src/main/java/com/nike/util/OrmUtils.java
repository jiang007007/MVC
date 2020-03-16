package com.nike.util;

import com.nike.parsebean.OrmHandle;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;

public class OrmUtils {

    public static <T> T query(String querSql, OrmHandle<T> ormHandle,Object... args){
        T newInstast= null;
        Connection conn;
        Statement statement;
        ResultSet rs;
        try {
           conn= DataSourcePool.getInstance().getConnection();
            statement = conn.createStatement();
            querSql= parseSql(querSql,args);
            rs = statement.executeQuery(querSql);
            newInstast = ormHandle.ormResultHandle(rs);
        }catch (Exception e){

        }finally {

        }

        return  newInstast;

    }
    //sql 解析
    private static String parseSql(String querSql,Object ...args){
        if (args.length ==0){
            return querSql;
        }
        StringBuilder strSql = new StringBuilder();
        String[] split = querSql.split(" ");
        int i =0;
        for (String str: split){

            if (str.contains("?")){
                Object argsIndex= args[i++];
               if (argsIndex instanceof Integer){
                   str = str.replace("?",String.valueOf(argsIndex));
               }else {
                   str ="\""+ str.replace("?",(String)argsIndex)+"\"";
               }
                strSql.append(str);
            }else {
                strSql.append(str).append(" ");
            }
        }
        return strSql.toString();
    }


    public static <T>T columnToProperies(ResultSet rs,Class<T> clazz) throws Exception {
        T instance = null;
        int [] result= null;
        ResultSetMetaData metaData =null;
        metaData=rs.getMetaData();
        // the first column is 1, the second is 2, etc.
        int columnCount = metaData.getColumnCount();
        result = new int[columnCount+1];
        Field[] declaredFields = clazz.getDeclaredFields();
        //属性与ResultMetaData的的咧对应关系
        for (int i=1;i < result.length;i++){
            for (int j =0; j < declaredFields.length;j++){
                if (declaredFields[j].getName().equalsIgnoreCase(metaData.getColumnName(i))){
                    result[i] = j;
                }
            }
        }

        //反射注入值
        instance=clazz.newInstance();
        for (int k =1; k < result.length;k++){
            if (result[k] !=-1){
                Field field=declaredFields[result[k]];
                field.setAccessible(true);
                if (clazz == Integer.class|| clazz == Integer.TYPE){
                    field.set(instance,rs.getInt(k));
                }else if (clazz == Byte.class|| clazz == Byte.TYPE){
                    field.set(instance,rs.getByte(k));
                }else if (clazz == Short.class || clazz== Short.TYPE){
                    field.set(instance,rs.getShort(k));
                }else if (clazz == Long.class || clazz == Long.TYPE){
                    field.set(instance,rs.getLong(k));
                }else if (clazz == String.class){
                    field.set(instance,rs.getString(k));
                }else if (clazz == Date.class){
                    field.set(instance,rs.getDate(k));
                }else {
                    System.out.println("未添加的类型"+ clazz);
                }
            }
        }

        return  instance;
    }
}
