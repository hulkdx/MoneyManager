/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.hulkdx.moneymanager.data.local.DatabaseHelper;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;

@Singleton
public class DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final DatabaseHelper mDatabaseHelper;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper) {
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public void saveUserInformation(String name, int initialMoney) {
        mPreferencesHelper.saveUserInformation(name, initialMoney);
    }

    public boolean checkLoggedIn() {
        return !mPreferencesHelper.getUserName().equals("");
    }
}
