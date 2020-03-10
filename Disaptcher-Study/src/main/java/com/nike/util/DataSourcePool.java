package com.nike.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DataSourcePool extends BaseDataSourceAdapter {
    private final  List<Connection> ConnPool = new LinkedList<>();
    private static volatile DataSourcePool instance;
    private final static int COUNT=10;
    private ReentrantLock lock = new ReentrantLock();

    private DataSourcePool(){
        for (int i=0;i< COUNT; i++){
            Connection conn = JDBCUtil.getConnection();
            Connection ProxyConn=(Connection) Proxy.newProxyInstance(DataSourcePool.class.getClassLoader()
                    ,new Class<?>[]{Connection.class}, new ConnectionInvocationHandler(conn, ConnPool));
            ConnPool.add(ProxyConn);
        }
    }
    public static DataSourcePool getInstance(){
                if (instance == null){
                    synchronized (DataSourcePool.class){
                        if (instance == null){
                            instance = new DataSourcePool();
                        }
                    }
                }
        return  instance;
    }
    @Override
    public Connection getConnection() {
          lock.lock();
          try {
              if (!ConnPool.isEmpty()){
                  return ConnPool.remove(0);
              }
          }finally {
              lock.unlock();
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
