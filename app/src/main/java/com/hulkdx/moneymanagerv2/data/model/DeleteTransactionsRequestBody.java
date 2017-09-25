package com.hulkdx.moneymanagerv2.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 25/09/2017.
 */

public class DeleteTransactionsRequestBody {
    @SerializedName("id")
    @Expose
    private long[] id;

    public DeleteTransactionsRequestBody(long[] id) {
        this.id = id;
    }
}
