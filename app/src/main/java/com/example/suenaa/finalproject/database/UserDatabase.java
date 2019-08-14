package com.example.suenaa.finalproject.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.example.suenaa.finalproject.database.util.DAO;
import com.example.suenaa.finalproject.model.User;
import com.example.suenaa.finalproject.model.UserSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by 婷 on 2018/1/3.
 */

public class UserDatabase {

    private static final String LOG_TAG = "UserDatabase";

    private StorIOSQLite userStor;
    public static final String USER_TABLE_NAME = "users";
    private static UserDatabase instance;
    private static DAO<User> mUserDAO;

    public static void initInstance(Context context){
        instance = new UserDatabase(context);
    }

    public static UserDatabase getInstance(){
        return instance;
    }


    public UserDatabase(Context context){
        userStor = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new userStorOpenHelper(context))
                .addTypeMapping(User.class, new UserSQLiteTypeMapping())
                .build();
        mUserDAO = new DAO<>(userStor, context, User.class);
    }

    public static DAO<User> getUserDAO(){
        return mUserDAO;
    }

    public ArrayList<User> getUserByName(String name){
        return new ArrayList<>(userStor.get()
        .listOfObjects(User.class)
        .withQuery(Query.builder()
        .table("users")
        .where("name = ?")
        .whereArgs(name)
        .build())
        .prepare()
        .executeAsBlocking());
    }


    private class userStorOpenHelper extends SQLiteOpenHelper {
        public userStorOpenHelper(Context context) {
            super(context, USER_TABLE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS users (\n"
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "name TEXT, "
                    + "password TEXT, "
                    + "src BLOB "
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
