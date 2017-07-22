package com.hulkdx.moneymanager.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/20/2017.
 */

public class Category extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private int colorIdInResource;

    public Category() {
    }

    public Category(String name, int colorIdInResource) {
        this.name = name;
        this.colorIdInResource = colorIdInResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHexColor() {
        return colorIdInResource;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
