package com.hulkdx.moneymanager.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.hulkdx.moneymanager.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_boilerplate_pref_file";
    public static final String USER_NAME = "name";
    public static final String USER_MONEY = "totalMoney";

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

    public int getUserMoney() {
        return mPref.getInt(USER_MONEY, 0);
    }


    public void saveUserInformation(String name, int initialMoney){
        mPref.edit().putString(USER_NAME, name).putInt(USER_MONEY, initialMoney).apply();
    }

}
