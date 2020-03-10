package com.nike.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil {
    private final  static  String RESOURCE="data.properties";
    private static Properties properties;

    static {
        try {
            InputStream resourceAsStream = JDBCUtil.class.getClassLoader().getResourceAsStream(RESOURCE);
            if (resourceAsStream == null){
                throw  new RuntimeException("在资源文件在没有找到指定的文件");
            }
            properties = new Properties();
            properties.load(resourceAsStream);
            Class.forName(JDBCUtil.getDriver());
        }catch (Exception e){
            throw  new RuntimeException("数据据资源文件读取失败");
        }
    }

    public  static Connection getConnection(){
        try {
            return DriverManager.getConnection(JDBCUtil.getUrl(), JDBCUtil.getUserName(), JDBCUtil.getPassWord());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }



    public  static  String getDriver(){
        String driver = properties.getProperty("jdbc.driver");
        if (driver == null){
            throw  new RuntimeException("没有配置jdbc的driver");
        }
        return  driver;
    }
    public  static  String getUrl(){
        String url = properties.getProperty("jdbc.url");
        if (url == null){
            throw  new RuntimeException("没有配置jdbc的url");
        }
        return  url;
    }

    public  static  String getUserName(){
        String username = properties.getProperty("jdbc.username");
        if (username == null){
            throw  new RuntimeException("没有配置jdbc的用户名");
        }
        return  username;
    }
    public static String getPassWord() {
        String password = properties.getProperty("jdbc.password");
        if (password == null){
            throw  new RuntimeException("没有配置jdbc的用户名");
        }
        return  password;
    }

    public static String getFilePath(){
        String path = properties.getProperty("jdbc.package.path");
        if (path == null){
            throw  new RuntimeException("没有设置JavaBean的包路路径");
        }
        return  path;
    }

    public static String getDataBase(){
        String url = JDBCUtil.getUrl();
        return url.substring(url.lastIndexOf("/")+1);
    }


}
