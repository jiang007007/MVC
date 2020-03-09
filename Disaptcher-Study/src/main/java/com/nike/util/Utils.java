package com.nike.util;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.*;
import java.net.FileNameMap;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库字段类型对应java数据类型映射关系
 */
public class Utils {
    public  final static Map<String,String> SQL_TYPE2JAVA_TYPE = new HashMap<>();
    public   final  static Map<String,String> IMPORT_PACK_MAP =new HashMap<>();

    static {
        // mysql 数据类型对应java的数据类型
        SQL_TYPE2JAVA_TYPE.put("INT UNSIGNED", "Integer");
        SQL_TYPE2JAVA_TYPE.put("VARCHAR", "String");
        SQL_TYPE2JAVA_TYPE.put("INT", "Integer");
        SQL_TYPE2JAVA_TYPE.put("TINYINT", "Byte");
        SQL_TYPE2JAVA_TYPE.put("CHAR", "String");
        SQL_TYPE2JAVA_TYPE.put("TIMESTAMP", "Date");
        SQL_TYPE2JAVA_TYPE.put("DATETIME", "Date");

        // java bean 导包
        IMPORT_PACK_MAP.put("DATETIME", "import java.util.Date;");
        IMPORT_PACK_MAP.put("TIMESTAMP", "import java.util.Date;");
    }

    /**
     *  根据表名生成类名  urp_name > UrpName
     */
    public static  String generateClassName(String tableName){
        StringBuilder className= new StringBuilder();
        String[] tableNames = tableName.split("_");
        for (String str: tableNames){
            className.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
        }
        return className.toString();
    }

    /**
     * 根据列名生成变属性名 User_name =>userName;
     */
    public  static String generateFiledName(String FiledName){
        StringBuilder filedName = new StringBuilder();
        String[] fileNames = FiledName.split("_");
        for (int i = 0; i <fileNames.length ; i++) {
            if (i ==0){
                filedName.append(fileNames[i].substring(0,1).toLowerCase())
                        .append(fileNames[i].substring(1));
            }else {
                filedName.append(fileNames[i].substring(0,1).toUpperCase())
                        .append(fileNames[i].substring(1));
            }
        }
        return filedName.toString();
    }

    public static String getfirstUp(String str){
        if ("".equalsIgnoreCase(str) || str == null)
            throw new RuntimeException("字符串不能为空");
        return str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
    }

    /**
     * 生成java文件目录
     */
    public  static String createDir()  {
        StringBuilder javaBeanPath = new StringBuilder();
        String filePath = JDBCUtil.getFilePath();
        try {
            String canonicalPath = new File("").getCanonicalPath();//当前工程目录 E:\git\MVC\Disaptcher-Study
            System.out.println(canonicalPath);
            //Disaptcher-Study
            javaBeanPath.append(canonicalPath).append(File.separator).append("src")
                    .append(File.separator).append("main").append(File.separator).append("java");
            String[] paths = filePath.split("\\.");
            for (String str:paths){
                javaBeanPath.append(File.separator).append(str);
            }
            File file = new File(javaBeanPath.toString());
            file.mkdirs();
            System.out.println("路径生成成功");
        }catch (Exception e){
            System.err.println("路径生成失败"+ e.getMessage());
        }

        return javaBeanPath.toString();
    }

    public static void writeClassJava(String path,String context){
        if (path == null)
            throw new RuntimeException("路径不能为空");
        if (context == null)
            throw new RuntimeException("内容不能为空");
        FileOutputStream outputStream = null;
        try {
            outputStream= new FileOutputStream(new File(path));
            outputStream.write(context.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
