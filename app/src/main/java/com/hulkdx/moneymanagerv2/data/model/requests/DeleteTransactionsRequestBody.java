package com.hulkdx.moneymanagerv2.data.model.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 25/09/2017.
 *
 * This class is needed since passing an array to retrofit interface is not possible to do without
 * annotations.
 *
 * https://stackoverflow.com/a/37699931/3996989
 * Note @Field(id[]) does not worked as well.
 */

public class DeleteTransactionsRequestBody {
    @SerializedName("id")
    @Expose
    private long[] id;

    public DeleteTransactionsRequestBody(long[] id) {
        this.id = id;
    }
}
