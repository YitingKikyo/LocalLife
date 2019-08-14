package com.example.suenaa.finalproject.model;

import com.example.suenaa.finalproject.database.util.StorModel;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 婷 on 2018/1/3.
 */

@StorIOSQLiteType(table = "posts")
public class Post implements StorModel {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());

    @StorIOSQLiteColumn(name = "id", key = true, ignoreNull = true)
    Integer id;

    @StorIOSQLiteColumn(name = "user_id")
    Integer user_id;

    @StorIOSQLiteColumn(name = "content")
    String content;

    @StorIOSQLiteColumn(name = "date")
    String date;

    @StorIOSQLiteColumn(name = "position")
    String position;

    @StorIOSQLiteColumn(name = "src")
    byte[] src;


    public Post(){}

    public Post(Integer user_id, String content, Date date, String position){
        this.user_id = user_id;
        this.content = content;
        this.date = DATE_FORMAT.format(date);
    }

    public Post(Integer user_id, String content, Date date, String position, byte[] src){
        this.user_id = user_id;
        this.content = content;
        this.date = DATE_FORMAT.format(date);
        this.src = src;
    }

    public Post(Integer user_id, String content, String position){
        this(user_id, content, new Date(), position);
    }

    public Post(Integer user_id, String content, String position, byte[] src){
        this(user_id, content, new Date(), position, src);
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

    public String getPosition(){
        return position;
    }
    public void setPosition(String position){
        this.position = position;
    }

    public byte[] getSrc(){
        return src;
    }
    public void setSrc(byte[] src){
        this.src = src;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;

        Post post = (Post)o;

        return id != null ? id.equals(post.id) : post.id == null;
    }
}
