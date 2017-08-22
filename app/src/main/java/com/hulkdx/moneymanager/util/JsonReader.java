package com.hulkdx.moneymanager.util;

import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/22/2017.
 */

public class JsonReader {

    public static String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("error");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
