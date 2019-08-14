package com.example.suenaa.finalproject.model;

/**
 * Created by 婷 on 2018/1/3.
 */

import com.example.suenaa.finalproject.database.util.StorModel;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = "likes")
public class Like implements StorModel {

    @StorIOSQLiteColumn(name = "id", key = true, ignoreNull = true)
    Integer id;

    @StorIOSQLiteColumn(name = "user_id")
    Integer user_id;

    @StorIOSQLiteColumn(name = "post_id")
    Integer post_id;

    public Like(){}

    public Like(Integer user_id, Integer post_id){
        this.user_id = user_id;
        this.post_id = post_id;
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

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;

        Like like = (Like) o;

        return id != null ? id.equals(like.id) : like.id == null;
    }
}
