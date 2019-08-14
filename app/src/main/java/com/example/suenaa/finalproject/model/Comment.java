package com.example.suenaa.finalproject.model;

/**
 * Created by 婷 on 2018/1/3.
 */

import com.example.suenaa.finalproject.database.util.StorModel;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@StorIOSQLiteType(table = "comments")
public class Comment implements StorModel {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());

    @StorIOSQLiteColumn(name = "id", key = true, ignoreNull = true)
    Integer id;

    @StorIOSQLiteColumn(name = "post_id")
    Integer post_id;

    @StorIOSQLiteColumn(name = "user_id")
    Integer user_id;

    @StorIOSQLiteColumn(name = "content")
    String content;

    @StorIOSQLiteColumn(name = "date")
    String date;

    public Comment(){}

    public Comment(Integer post_id, Integer user_id, String content, Date date){
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.date = DATE_FORMAT.format(date);
    }

    public Comment(Integer post_id, Integer user_id, String content){
        this(post_id, user_id, content, new Date());
    }

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public Integer getUser_id(){
        return user_id;
    }
    public void setUser_id(Integer user_id){
        this.user_id = user_id;
    }

    public  Integer getPost_id(){
        return post_id;
    }
    public void setPost_id(Integer post_id){
        this.post_id = post_id;
    }

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;

        Comment comment = (Comment) o;

        return id != null ? id.equals(comment.id) : comment.id == null;
    }
}
