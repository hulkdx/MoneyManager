package com.hulkdx.moneymanagerv2.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/8/2017.
 */

public class Transaction extends RealmObject {

    @PrimaryKey private long id;
    // date format: day month year
    private String date;
    private Category category;
    private float amount;
    private String attachment;

    public Transaction(){}

    public Transaction(String date, float amount, String attachment) {
        this.date = date;
        this.amount = amount;
        this.attachment = attachment;
    }
    public float getAmount() {
        return amount;
    }

    public boolean isAmountPositive() {
        return amount > 0;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
