/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanagerv2.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.hulkdx.moneymanagerv2.data.local.DatabaseHelper;
import com.hulkdx.moneymanagerv2.data.local.DatabaseHelper.Transaction_Fields;
import com.hulkdx.moneymanagerv2.data.local.PreferencesHelper;
import com.hulkdx.moneymanagerv2.data.model.Category;
import com.hulkdx.moneymanagerv2.data.model.requests.DeleteTransactionsRequestBody;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.data.model.TransactionResponse;
import com.hulkdx.moneymanagerv2.data.model.User;
import com.hulkdx.moneymanagerv2.data.model.requests.UpdateTransactionRequest;
import com.hulkdx.moneymanagerv2.data.remote.HulkService;

import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    /************************* Auth Section *************************/

    public boolean checkLoggedIn() {
        return !mPreferencesHelper.getUserName().equals("");
    }

    public Flowable<User> login(String username, String password) {
        return mHulkService.postLogin(username, password);
    }

    public Flowable<User> register(String username,
                                   String password,
                                   String email,
                                   String currency) {

        return mHulkService.postRegister(username, password, email, email, currency);
    }

    /************************* Transactions Section *************************/
    public Flowable<List<Transaction>> getTransactions() {
        return mDatabaseHelper.getTransactions().distinct();
    }

    public Flowable<Transaction> addTransaction(Transaction newTransaction, long categoryId) {

        if (getPreferencesHelper().isSync()) {
            return mHulkService
                    .createTransaction("JWT " + getPreferencesHelper().getToken(),
                                      newTransaction.getAmount(),
                                      newTransaction.getDate(),
                                      newTransaction.getAttachment(),
                                      categoryId == -1 ? null : String.valueOf(categoryId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .concatMap(transaction ->
                            mDatabaseHelper.addTransaction(transaction, categoryId));
        }

        return mDatabaseHelper.addTransaction(newTransaction, categoryId).distinct();
    }

    public Flowable<List<Transaction>> searchTransactionWithDate(int day, int month, int year,
                                                                 int isDailyOrMonthlyOrYearly) {
        return mDatabaseHelper.searchTransactionWithDate(day, month, year,
                isDailyOrMonthlyOrYearly);
    }

    public Flowable<TransactionResponse> deleteTransactions(long[] selectedIds) {

        if (getPreferencesHelper().isSync()) {
            return mDatabaseHelper.removeTransactions(selectedIds, true)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .concatMap(
                            transactionResponse ->
                                    mHulkService.deleteTransaction(
                                            "JWT " + getPreferencesHelper().getToken(),
                                            new DeleteTransactionsRequestBody(selectedIds))
                    )
                    .subscribeOn(Schedulers.io());
        }

        return mDatabaseHelper.removeTransactions(selectedIds, false);

    }

    public Flowable<Object> updateTransaction(long transactionId,
                                              Transaction_Fields[] key,
                                              Object[] value) {

        if (getPreferencesHelper().isSync()) {
            UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId);
            request.updateData(key, value);
            return mHulkService.updateTransaction("JWT " + getPreferencesHelper().getToken(),
                    request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(o -> mDatabaseHelper.updateTransaction(transactionId, key, value));
        }
        return mDatabaseHelper.updateTransaction(transactionId, key, value);
    }

    public Flowable<TransactionResponse> syncTransactions(String token) {
        return mHulkService.getTransactions("JWT " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(mDatabaseHelper::addTransactions);
    }
    /************************* Category Section *************************/
    public Flowable<List<Category>> getCategories() {
        return mDatabaseHelper.getCategories();
    }

    public Flowable<Category> addCategory(Category newCategory) {

        if (getPreferencesHelper().isSync()) {
            return mHulkService
                    .createCategory("JWT " + getPreferencesHelper().getToken(),
                                    newCategory.getName(),
                                    newCategory.getHexColor())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .concatMap(mDatabaseHelper::addCategory);
        }

        return mDatabaseHelper.addCategory(newCategory);
    }


    public Flowable<List<Category>> syncCategories(String token) {
        return mHulkService.getCategories("JWT " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(mDatabaseHelper::addCategories);
    }


}
