package io.bxbxbai.zhuanlan.core;

import android.content.Context;
import orm.LiteOrm;
import orm.db.DataBase;
import orm.db.annotation.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data center
 *
 *  一个单例设计模式
 *  保证全局只有一个数据库的操作实例
 * @author bxbxbai
 */
public final class DataCenter {

    private static DataCenter instance;

    private DataBase db;
//  单例模式要求私有的构造方法
    private DataCenter(Context context, String dbName) {
        db = LiteOrm.newSingleInstance(context, dbName);
    }
//这是一个懒汉模式的单例，一个静态方法
    public static void init(Context context, String dbName) {
        if (instance == null) {
            synchronized (DataCenter.class) {
                if (instance == null) {
                    instance = new DataCenter(context.getApplicationContext(), dbName);//生命周期是应用的生命周期
                }
            }
        }
    }

    public static DataCenter instance() {
        if (instance == null) {
            throw new IllegalStateException("you must call LiteManager.init(context, dbName) first");
        }
        return instance;
    }
//  查询数目
    public <T> long queryCount(Class<T> clazz) {
        return db.queryCount(clazz);
    }
//  删除数据库中该类型数据
    public <T> int clear(Class<T> clazz) {
        return db.delete(clazz);
    }
//  保存数据
    public void save(Object o) {
        db.save(o);
    }//保存操作

    /**
     * 查询数据库中所有该类型数据
     * @param clazz  查询数据的类型
     * @param <T>
     */
    public <T> List<T> queryAll(Class<T> clazz) {
        List<T> list = db.query(clazz);
        return list == null ? new ArrayList<T>() : list;
    }

    private class CacheContainer<T> extends HashMap<Class<T>, Map<String, T>> {


    }

}
