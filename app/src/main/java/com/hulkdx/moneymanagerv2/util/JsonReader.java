package com.hulkdx.moneymanagerv2.util;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/22/2017.
 */

public class JsonReader {

    public static String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());

            if (jsonObject.has("error")) {
                return jsonObject.getString("error");
            } else if (jsonObject.has("username")) {
                // The server returns json array.
                JSONArray jsonArray = (JSONArray) jsonObject.get("username");
                return jsonArray.get(0).toString();
            } else if (jsonObject.has("password")) {
                return jsonObject.getString("password");
            } else if (jsonObject.has("email")) {
                return jsonObject.getString("email");
            }
            return "undefined error";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
