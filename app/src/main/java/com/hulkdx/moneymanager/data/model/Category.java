package com.hulkdx.moneymanager.data.model;

import io.realm.RealmObject;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/20/2017.
 */

public class Category extends RealmObject {

    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
