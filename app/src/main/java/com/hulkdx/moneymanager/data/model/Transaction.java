package com.hulkdx.moneymanager.data.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/8/2017.
 */

public class Transaction extends RealmObject {
    
    private String day;
    private String month;
    private String year;
    private Category category;
    private float amount;

    @Index
    public long categoryId;

    public Transaction(){}

    public Transaction(String day, String month, String year, Category category, float amount) {
        this.day = day;
        this.month = month;
        this.year = year;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
