/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.hulkdx.moneymanager.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_hulkdx_pref";
    public static final String USER_NAME = "name";
    public static final String USER_MONEY = "totalMoney";
    public static final String SAVED_CURRENCY = "saved_currency";
    public static final String SYNC = "sync";
    public static final String USER_TOKEN = "user_token";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public String getUserName() {
        return mPref.getString(USER_NAME, "");
    }

    public float getUserMoney() {
        return mPref.getFloat(USER_MONEY, 0);
    }

    public String getCurrencyName() {
        return mPref.getString(SAVED_CURRENCY, "EUR");
    }

    public void saveUserInformation(String name, float initialMoney, String currency){
        mPref.edit()
                .putString(USER_NAME, name)
                .putFloat(USER_MONEY, initialMoney)
                .putString(SAVED_CURRENCY, currency).apply();
    }

    public void setUserMoney(float userMoney) {
        mPref.edit().putFloat(USER_MONEY, userMoney).apply();
    }

    // Add amount to existing amount.
    public float updateBalance(float amount) {
        float newAmount = getUserMoney() + amount;
        mPref.edit().putFloat(USER_MONEY, newAmount).apply();
        return newAmount;
    }

    public void saveSync(boolean isSynced) {
        mPref.edit().putBoolean(SYNC, isSynced).apply();
    }

    public boolean getSync(){
        return mPref.getBoolean(SYNC, false);
    }

    public void saveToken(String token) {
        mPref.edit().putString(USER_TOKEN, token).apply();
    }

    public String getToken(){
        return mPref.getString(USER_TOKEN, "");
    }
}
