package com.example.suenaa.finalproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.suenaa.finalproject.database.util.DAO;
import com.example.suenaa.finalproject.model.Comment;
import com.example.suenaa.finalproject.model.CommentSQLiteTypeMapping;
import com.example.suenaa.finalproject.model.Follow;
import com.example.suenaa.finalproject.model.FollowSQLiteTypeMapping;
import com.example.suenaa.finalproject.model.Like;
import com.example.suenaa.finalproject.model.LikeSQLiteTypeMapping;
import com.example.suenaa.finalproject.model.Post;
import com.example.suenaa.finalproject.model.PostSQLiteTypeMapping;
import com.example.suenaa.finalproject.model.User;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by 婷 on 2018/1/3.
 */

public class PostDatabase {

    private static final String LOG_TAG = "PostDatabase";

    private StorIOSQLite postStor;
    private static PostDatabase instance;
    private static final String DATABASE_NAME = "postdatabase";

    private static DAO<Post> mPostDAO;
    private static DAO<Comment> mCommentDAO;
    private static DAO<Like> mLikeDAO;
    private static DAO<Follow> mFollowDAO;


    public static void initInstance(Context context){
        if(instance == null){
            instance = new PostDatabase(context);
        }
    }

    public static PostDatabase getInstance(){
        return instance;
    }

    public PostDatabase(Context context){
        postStor = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new PostStorOpenHelper(context))
                .addTypeMapping(Comment.class, new CommentSQLiteTypeMapping())
                .addTypeMapping(Follow.class, new FollowSQLiteTypeMapping())
                .addTypeMapping(Like.class, new LikeSQLiteTypeMapping())
                .addTypeMapping(Post.class, new PostSQLiteTypeMapping())
                .build();
        mPostDAO = new DAO<>(postStor, context, Post.class);
        mCommentDAO = new DAO<>(postStor, context, Comment.class);
        mLikeDAO = new DAO<>(postStor, context, Like.class);
        mFollowDAO = new DAO<>(postStor, context, Follow.class);
    }

    public static DAO<Post> getPostDAO(){
        return mPostDAO;
    }

    public static DAO<Comment> getCommentDAO(){
        return mCommentDAO;
    }

    public static DAO<Like> getLikeDAO(){
        return mLikeDAO;
    }

    public static DAO<Follow> getFollowDAO(){
        return mFollowDAO;
    }

    public ArrayList<Post> getPosts(int userId){
        return new ArrayList<>(postStor.get()
        .listOfObjects(Post.class)
        .withQuery(Query.builder()
        .table("posts")
        .where("user_id = ?")
        .whereArgs(userId)
        .build())
        .prepare().executeAsBlocking());
    }

    public ArrayList<Comment> getCommentList(int postId){
        return new ArrayList<>(postStor.get()
        .listOfObjects(Comment.class)
        .withQuery(Query.builder()
        .table("comments")
        .where("post_id = ?")
        .whereArgs(postId)
        .build())
        .prepare().executeAsBlocking());
    }


    public ArrayList<Follow> getFollowingList(int userId){
        return new ArrayList<>(postStor.get()
                .listOfObjects(Follow.class)
                .withQuery(Query.builder()
                .table("follows")
                .where("follower_id = ?")
                .whereArgs(userId)
                .build())
                .prepare().executeAsBlocking());
    }

    public ArrayList<Follow> getFollowedList(int userId){
        return new ArrayList<>(postStor.get()
        .listOfObjects(Follow.class)
        .withQuery(Query.builder()
        .table("follows")
        .where("followed_id = ?")
        .whereArgs(userId)
        .build())
        .prepare().executeAsBlocking());
    }

    public ArrayList<Like> getLikeList(int postId) {
        return new ArrayList<>(postStor.get()
                .listOfObjects(Like.class)
                .withQuery(Query.builder()
                        .table("likes")
                        .where("post_id = ?")
                        .whereArgs(postId)
                        .build())
                .prepare().executeAsBlocking());
    }



    private class PostStorOpenHelper extends SQLiteOpenHelper {
        public PostStorOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS posts (\n"
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "user_id INTEGER, "
                    + "content TEXT, "
                    + "date TEXT, "
                    + "position TEXT, "
                    + "src BLOB "
                    + ");"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS likes (\n"
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "user_id INTEGER, "
                    + "post_id INTEGER "
                    + ");"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS comments (\n"
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "post_id INTEGER, "
                    + "user_id INTEGER, "
                    + "content TEXT, "
                    + "date TEXT"
                    + ");"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS follows (\n"
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "follower_id INTEGER, "
                    + "followed_id INTEGER "
                    + ");"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
