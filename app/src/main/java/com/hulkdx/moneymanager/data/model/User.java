package com.hulkdx.moneymanager.data.model;

import io.realm.RealmObject;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/18/2017.
 */

public class User extends RealmObject {

    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String token;
    private String error;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
