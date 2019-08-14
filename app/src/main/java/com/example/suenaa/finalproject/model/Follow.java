package com.example.suenaa.finalproject.model;

/**
 * Created by 婷 on 2018/1/3.
 */

import com.example.suenaa.finalproject.database.util.StorModel;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import java.io.Serializable;

@StorIOSQLiteType(table = "follows")
public class Follow implements StorModel, Serializable {

    @StorIOSQLiteColumn(name = "id", key = true, ignoreNull = true)
    Integer id;

    @StorIOSQLiteColumn(name = "follower_id")
    Integer follower_id;

    @StorIOSQLiteColumn(name = "followed_id")
    Integer followed_id;

    public Follow(){}

    public Follow(Integer follower_id, Integer followed_id){
        this.follower_id = follower_id;
        this.followed_id = followed_id;
    }

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public Integer getFollower_id(){
        return follower_id;
    }
    public void setFollower_id(Integer follower_id){
        this.follower_id = follower_id;
    }

    public  Integer getFollowed_id(){
        return followed_id;
    }
    public void setFollowed_id(Integer followed_id){
        this.followed_id = followed_id;
    }

    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if(o == null || o.getClass() != this.getClass()) return false;

        Follow follow = (Follow) o;

        return id != null ? id.equals(follow.id) : follow.id == null;
    }

}
