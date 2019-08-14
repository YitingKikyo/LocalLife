package com.example.suenaa.finalproject.model;

import com.example.suenaa.finalproject.database.util.StorModel;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by user on 2018/1/2.
 */

@StorIOSQLiteType(table = "users")
public class User implements StorModel {

    @StorIOSQLiteColumn(name = "id", key = true, ignoreNull = true)
    Integer id;

    @StorIOSQLiteColumn(name = "name")
    String name;

    @StorIOSQLiteColumn(name = "password")
    String password;

    @StorIOSQLiteColumn(name = "src")
    byte[] src; //头像，视情决定做不做

    public User(){}

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, byte[] src){
        this.name = name;
        this.password = password;
        this.src = src;
    }

    public byte[] getSrc() {
        return src;
    }
    public void setSrc(byte[] src){
        this.src = src;
    }

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }


    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

}
