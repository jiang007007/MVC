package com.nike.util;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;

public class OrmUtils {
    public static <T> T newIntast(Class<T> clazz,String querSql){
        T newInstast= null;
        Field[] fields;
        Connection conn;
        Statement statement;
        ResultSet rs;
        ResultSetMetaData metaData;
        try {
           conn= DataSourcePool.getInstance().getConnection();
            statement = conn.createStatement();
            rs = statement.executeQuery(querSql);
            //有结果集
            while (rs.next()){
                metaData = rs.getMetaData();
                newInstast = clazz.newInstance();
                fields = clazz.getDeclaredFields();
                int[] columnPropertes = columnPropertes(fields, metaData);
                for (int i=1; i< columnPropertes.length;i++){
                    if (columnPropertes[i] ==-1)
                        continue;
                    Field field=fields[columnPropertes[i]];
                    Class<?> clazzType = field.getType();
                    field.setAccessible(true);
                    if (clazzType == Integer.TYPE ||clazzType == Integer.class){
                        field.set(newInstast,rs.getInt(i));
                    }else if (clazzType == Byte.TYPE || clazzType == Byte.class){
                        field.set(newInstast,rs.getByte(i));
                    }else if (clazzType == String.class){
                        field.set(newInstast,rs.getString(i));
                    }else if (clazzType == Date.class){
                        field.set(newInstast,rs.getDate(i));
                    }else {
                        System.out.println("未添加的类型"+ clazzType);
                    }
                }
            }
        }catch (Exception e){

        }finally {

        }

        return  newInstast;

    }
    private static int[] columnPropertes(Field[] fields, ResultSetMetaData metaData){
        int [] result= null;
        try {
            int columnCount = metaData.getColumnCount();
            result = new int[columnCount +1];
            Arrays.fill(result,-1);
            for (int i = 1; i <result.length ; i++) {
                for (int j =0; j <fields.length ; j++) {
                    if (fields[j].getName().equalsIgnoreCase(metaData.getColumnName(i))){
                        result[i] = j;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("数据库访问失败");
        }
        return result;
    }
}
