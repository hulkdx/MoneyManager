package com.hulkdx.moneymanagerv2.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 12/09/2017.
 * This file is with additional information such as amount_count
 */

public class TransactionResponse {
    @SerializedName("amount_count")
    @Expose
    private float amountCount;
    @SerializedName("response")
    @Expose
    private List<Transaction> transactions = null;

    public float getAmountCount() {
        return amountCount;
    }

    public void setAmountCount(float amountCount) {
        this.amountCount = amountCount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
