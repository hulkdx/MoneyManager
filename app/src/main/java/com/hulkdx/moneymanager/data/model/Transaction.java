package com.hulkdx.moneymanager.data.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/8/2017.
 */

public class Transaction extends RealmObject {

    @PrimaryKey private long id;
    private int day;
    private int month;
    private int year;
    private Category category;
    private float amount;
    private String attachment;

    public Transaction(){}

    public Transaction(int day, int month, int year, float amount, String attachment) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.attachment = attachment;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getAmount() {
        return amount;
    }

    public String getAmountString() {
        return String.valueOf(amount);
    }

    public boolean isAmountPositive() {
        return amount > 0;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
}
