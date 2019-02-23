package com.hulkdx.moneymanagerv2.data.model.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hulkdx.moneymanagerv2.data.local.DatabaseHelper.Transaction_Fields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 29/12/2017.
 */
public class UpdateTransactionRequest {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("updateData")
    @Expose
    private List<HashMap<String, Object>> updateData;

    public UpdateTransactionRequest(long id, List<HashMap<String, Object>> updateData) {
        this.id = id;
        this.updateData = updateData;
    }

    public UpdateTransactionRequest(long id) {
        this.id = id;
    }

    public void updateData(Transaction_Fields[] keys, Object[] values) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        for (int i=0, len=keys.length; i<len; i++) {
            Transaction_Fields key_code = keys[i];
            Object value = values[i];
            HashMap<String, Object> hashMap = new HashMap<>();
            String key;
            switch (key_code) {
                case DATE:
                    key = "date";
                    break;
                case AMOUNT:
                    key = "amount";
                    break;
                case CATEGORY:
                    key = "category";
                    break;
                case ATTACHMENT:
                    key = "attachment";
                    if (value == null) value = "";
                    break;
                default:
                    Timber.e("error updateData: key is not defined?!");
                    key = "";
                    break;
            }
            hashMap.put(key, value);
            list.add(hashMap);
        }
        updateData = list;
    }
}
