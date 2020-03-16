package com.nike.parsebean;

import java.sql.ResultSet;

/**
 *   抽象返回 结果是单个还是集合
 * @param <T>
 */
public interface OrmHandle<T> {
    T  ormResultHandle(ResultSet set) throws Exception;
}
