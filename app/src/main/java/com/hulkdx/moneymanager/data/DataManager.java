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
import java.util.List;
import io.reactivex.Flowable;

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

    public boolean checkLoggedIn() {
        return !mPreferencesHelper.getUserName().equals("");
    }

    public Flowable<List<Transaction>> getTransactions() {
        return mDatabaseHelper.getTransactions().distinct();
    }

    public Flowable<Transaction> addTransaction(Transaction newTransaction, long CategoryId) {
        return mDatabaseHelper.addTransaction(newTransaction, CategoryId).distinct();
    }

    public Flowable<List<Category>> getCategories() {
        return mDatabaseHelper.getCategories();
    }

    public Flowable<Category> addCategory(Category newCategory) {
        return mDatabaseHelper.addCategory(newCategory);
    }

    public Flowable<List<Transaction>> searchTransactionWithDate(String day, String month, String year,
                                                                 int isDailyOrMonthlyOrYearly) {
        return mDatabaseHelper.searchTransactionWithDate(day, month, year, isDailyOrMonthlyOrYearly);
    }
}
