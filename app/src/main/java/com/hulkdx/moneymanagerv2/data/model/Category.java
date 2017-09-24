package com.hulkdx.moneymanagerv2.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/20/2017.
 */

public class Category extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String hexColor;

    public Category() {
    }

    public Category(String name, String hexColor) {
        this.name = name;
        this.hexColor = hexColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHexColor() {
        return hexColor;
    }
}
