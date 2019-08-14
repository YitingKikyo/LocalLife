package com.example.suenaa.finalproject.database.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 婷 on 2018/1/3.
 */

public class DAO<T extends StorModel> {

    public static final int ACTION_INSERT = 1;
    public static final int ACTION_UPDATE = 2;
    public static final int ACTION_DELETE = 3;


    public static class DatabaseChangeEvent<T extends StorModel> {
        private T mData;
        private int mAction;

         DatabaseChangeEvent(T data, int action) {
            mData = data;
            mAction = action;
        }

        public T getData() {
            return mData;
        }

        public int getAction() {
            return mAction;
        }
    }

    private static final String PREF_NAME = DAO.class.getName();

    private final StorIOSQLite mStorIOSQLite;
    private final AtomicInteger mMaxId;
    private final SharedPreferences mSharedPreferences;
    private final String mIdKey;
    private final EventBus mEventBus;
    private final Class<T> mModelClass;
    private final String mTable;

    public DAO(StorIOSQLite storIOSQLite, Context context, Class<T> modelClass) {
        mStorIOSQLite = storIOSQLite;
        mModelClass = modelClass;
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mIdKey = modelClass.getName();
        mMaxId = new AtomicInteger(mSharedPreferences.getInt(mIdKey, 0));
        mEventBus = EventBus.getDefault();
        mTable = getTable(modelClass);

    }

    public boolean insert(T item){
        //首选item便是要插入到数据库的内容，但是，我们需要调用他的setId方法
        //但是这是一个泛型，并没有一个叫setId的方法
        //但我们的数据库的对象都应该有这个方法才能使用我们这个Helper
        //因此我们抽象出一个接口
        item.setId(getAndIncreaseId());
        if(mStorIOSQLite.put()
                .object(item)
                .prepare()
                .executeAsBlocking()
                .wasInserted()){
            mEventBus.post(new DatabaseChangeEvent<>(item, ACTION_INSERT));
            return true;
        }
        return false;
    }

    public boolean update(T item){
        if(mStorIOSQLite.put()
                .object(item)
                .prepare()
                .executeAsBlocking()
                .wasUpdated()){
            mEventBus.post(new DatabaseChangeEvent<>(item, ACTION_UPDATE));
            return true;
        }
        return false;
    }

    public T getById(int id){
       return mStorIOSQLite.get()
                .object(mModelClass)
                .withQuery( Query.builder()
                    .table(mTable)
                    .where("id = ?")
                    .whereArgs(id)
                    .build())
                .prepare()
                .executeAsBlocking();
    }

    public boolean deleteById(int id){
        T item = getById(id);
        if(item == null)
            return false;
        if(mStorIOSQLite.delete()
                .byQuery(DeleteQuery.builder()
                .table(mTable)
                .where("id = ?")
                .whereArgs(id)
                .build())
                .prepare()
                .executeAsBlocking()
                .numberOfRowsDeleted() > 0){
            mEventBus.post(new DatabaseChangeEvent<>(item, ACTION_DELETE));
            return true;
        }
        return false;
    }


    public boolean delete(T item) {
        if(mStorIOSQLite.delete()
                .byQuery(DeleteQuery.builder()
                        .table(mTable)
                        .where("id = ?")
                        .whereArgs(item.getId())
                        .build())
                .prepare()
                .executeAsBlocking()
                .numberOfRowsDeleted() > 0){
            mEventBus.post(new DatabaseChangeEvent<>(item, ACTION_DELETE));
            return true;
        }
        return false;
    }


    //我们这个ModelClass是User, Comment之类的Class，我们对他们的table的指定是用注解指定的
    //现在我们可以顺便展示一下，这些注解是如何工作的，也就是，如何从一个类获取他的注解和详细信息
    private String getTable(Class<T> modelClass) {
        StorIOSQLiteType annotation = modelClass.getAnnotation(StorIOSQLiteType.class);
        if(annotation == null)
            throw new IllegalArgumentException("model class should has StorIOSQLiteType");
        return annotation.table();
    }


    private int getAndIncreaseId() {
        int id = mMaxId.getAndIncrement();
        mSharedPreferences.edit().putInt(mIdKey, mMaxId.get()).apply();
        return id;
    }
}
