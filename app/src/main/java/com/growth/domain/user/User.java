package com.growth.domain.user;

/**
 * Created by SSL-D on 2016-08-31.
 */

public class User {
    int id;
    String usercode;
    public static User instance=null;

    public static User getInstance(){
        if(instance == null)
            instance = new User();
        return instance;
    }

    public int getId() {
        return id;
    }

    public String getUserCode() {
        return usercode;
    }

    public void setUserCode(String userCode) {
        this.usercode = userCode;
    }

    public void setId(int id) {
        this.id = id;
    }
}
