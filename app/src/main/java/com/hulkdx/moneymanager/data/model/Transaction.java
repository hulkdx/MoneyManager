package com.hulkdx.moneymanager.data.model;

import io.realm.RealmObject;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/8/2017.
 */

public class Transaction extends RealmObject {
    
    private String day;
    private String month;
    private String year;
    private String category;
    private float amount;

    public Transaction(){}

    public Transaction(String day, String month, String year, String category, float amount) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.category = category;
        this.amount = amount;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
