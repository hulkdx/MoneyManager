/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.data;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.hulkdx.moneymanager.data.local.DatabaseHelper;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.data.model.User;
import com.hulkdx.moneymanager.data.remote.HulkService;
import java.util.List;
import io.reactivex.Flowable;

@Singleton
public class DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final DatabaseHelper mDatabaseHelper;
    private final HulkService mHulkService;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper,
                       HulkService hulkService) {
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
        mHulkService = hulkService;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public boolean checkLoggedIn() {
        return !mPreferencesHelper.getUserName().equals("");
    }

    public Flowable<List<Transaction>> getTransactions() {
        return mDatabaseHelper.getTransactions().distinct();
    }

    public Flowable<Transaction> addTransaction(Transaction newTransaction, long categoryId) {
        return mDatabaseHelper.addTransaction(newTransaction, categoryId).distinct();
    }

    public Flowable<List<Category>> getCategories() {
        return mDatabaseHelper.getCategories();
    }

    public Flowable<Category> addCategory(Category newCategory) {
        return mDatabaseHelper.addCategory(newCategory);
    }

    public Flowable<List<Transaction>> searchTransactionWithDate(int day, int month, int year,
                                                                 int isDailyOrMonthlyOrYearly) {
        return mDatabaseHelper.searchTransactionWithDate(day, month, year, isDailyOrMonthlyOrYearly);
    }

    public Flowable<User> login(String username, String password) {
        return mHulkService.postLogin(username, password);
    }

    public Flowable<User> register(String username, String password, String email) {
        return mHulkService.postRegister(username, password, email, email);
    }
}
