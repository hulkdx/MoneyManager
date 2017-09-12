package com.hulkdx.moneymanager.data.model;

import java.util.List;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 12/09/2017.
 * This file is with additional information such as amount_count
 */

public class TransactionResponse {
    private float amount_count;
    private List<Transaction> response = null;

    public float getAmountCount() {
        return amount_count;
    }

    public void setAmountCount(float amountCount) {
        this.amount_count = amountCount;
    }

    public List<Transaction> getResponse() {
        return response;
    }

    public void setResponse(List<Transaction> response) {
        this.response = response;
    }
}
