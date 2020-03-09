package com.nike.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DataSourcePool extends BaseDataSourceAdapter {
    private final  List<Connection> ConnPool = new LinkedList<>();
    private final static int COUNT=10;
    //初始化10个连接
      {
        for (int i=0;i< COUNT; i++){
            Connection conn = JDBCUtil.getConnection();
            Connection ProxyConn=(Connection) Proxy.newProxyInstance(DataSourcePool.class.getClassLoader()
                    ,new Class<?>[]{Connection.class}, new ConnectionInvocationHandler(conn, ConnPool));
            ConnPool.add(ProxyConn);
        }
    }
    @Override
    public  Connection getConnection() throws SQLException {
        if (!ConnPool.isEmpty()){
            return ConnPool.remove(0);
        }
        System.err.println("连接池中的连接已用完！！！");
        return null;
    }

     static class ConnectionInvocationHandler implements InvocationHandler{
        private Connection conn;
        private List<Connection> pool;
        ConnectionInvocationHandler(Connection conn, List<Connection> pool){
            this.conn = conn;
            this.pool = pool;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("close".equalsIgnoreCase(method.getName())){
                pool.add(conn);
                return  null;
            }
            return method.invoke(conn,args);
        }
    }
}
