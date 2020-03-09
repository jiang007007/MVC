package com.nike.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 通过数据库结果集创建javabean
 */
public class CreateJavaBean {
    private Connection conn;
    private DatabaseMetaData metaData;
    public void init(){
        try {
            conn= JDBCUtil.getConnection();
            assert conn != null;
            metaData= conn.getMetaData();
            execGenerate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void  execGenerate(){
        ResultSet tables = null;
        try {
            tables =metaData.getTables(JDBCUtil.getDataBase(),null,null,null);
            while (tables.next()){
                //表名
                String tableName = tables.getString(3);
                ResultSet columns = metaData.getColumns(JDBCUtil.getDataBase(), JDBCUtil.getDataBase(), tableName, null);
                Map<String,String> dataMap = new HashMap<>();
                while (columns.next()){
//                    System.out.print(columns.getString("COLUMN_NAME"));//列名
//                    System.out.println(columns.getString("DATA_TYPE"));
//                    System.out.println(columns.getString("TYPE_NAME"));// 数据类型、
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    dataMap.put(columnName,dataType);
                }

                /**
                 * 产生类
                 * package  xxxx.xxx.xxx
                 * import java.util.map;
                 * public class A {
                 *     private int a;
                 *     public void setA(int a){
                 *         this.a = a;
                 *     }
                 *     public int getA(){
                 *         return a;
                 *     }
                 * }
                 */
                StringBuilder sb = new StringBuilder();
                Set<Map.Entry<String, String>> entries = dataMap.entrySet();
                sb.append("package ").append(JDBCUtil.getFilePath()).append("; \n");
                //数据类型导报
                for (Map.Entry<String,String> entry: entries){
                    String value = entry.getValue();
                    String nesImport = Utils.IMPORT_PACK_MAP.get(value);
                    if (nesImport !=null)
                        sb.append(nesImport + "\n");
                }
                //生产public class xx {
                String generateClassName = Utils.generateClassName(tableName);
                sb.append("public class ").append(generateClassName).append("{").append("\n");
                //生产 private int xx;
                for (Map.Entry<String,String> entry: entries){
                    String filedName = entry.getKey();
                    String daType = entry.getValue();
                    sb.append("\t" + "private " ).append(Utils.SQL_TYPE2JAVA_TYPE.get(daType)).append(" ").append(Utils.generateFiledName(filedName)).append(";"+"\n");
                }
                //gettersetter方法
                for (Map.Entry<String,String> entry:entries){
                    String filedName = entry.getKey();
                    String daType = entry.getValue();
                    //public int getA(){return a}
                    sb.append("\t" + "public ").append(Utils.SQL_TYPE2JAVA_TYPE.get(daType)).append(" get").append(Utils.getfirstUp(filedName)).append("()"  + "{")
                            .append("\n\t\t").append("return"+" ").append(Utils.generateFiledName(filedName)).append(";"+"\n").append("\t").append("}");
                    // public void setA(int a){this.a a}
                    sb.append("\n\t").append("public void ").append("set").append(Utils.getfirstUp(filedName)).append("(").append(Utils.SQL_TYPE2JAVA_TYPE.get(daType))
                            .append(" ").append(Utils.generateFiledName(filedName)).append("){").append("\n\t\t").append("this.").append(Utils.generateFiledName(filedName))
                            .append("=").append(" ").append(Utils.generateFiledName(filedName)).append(";").append("\n\t}").append("\n");
                }
                sb.append("\n").append("}");
                Utils.writeClassJava( Utils.createDir()+ File.separator+generateClassName+".java",sb.toString());
            }
        }catch (Exception e){


        }
    }

}
