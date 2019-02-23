/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanagerv2.ui.main;

import android.os.HandlerThread;

import com.hulkdx.moneymanagerv2.data.DataManager;
import com.hulkdx.moneymanagerv2.data.local.DatabaseHelper.Transaction_Fields;
import com.hulkdx.moneymanagerv2.data.model.Category;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


public class MainPresenter extends BasePresenter<MainMvpView> {

    private CompositeDisposable mDisposables;
    private final DataManager mDataManager;
    private Scheduler mBackgroundSchedulers;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);

        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();
        mBackgroundSchedulers = AndroidSchedulers.from(thread.getLooper());
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposables != null) mDisposables.clear();
    }

    public String getCurrencyName() {
        return mDataManager.getPreferencesHelper().getCurrencyName();
    }
    /**
     * Load transactions from Database.
     */
    public void loadTransactions() {
        mDisposables.add(
                mDataManager.getTransactions()
                .subscribeOn(mBackgroundSchedulers)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactions -> {
                            Timber.i("onNext loadTransactions");
                            getMvpView().setBalanceTextView(
                                    mDataManager.getPreferencesHelper().getUserMoney());
                            getMvpView().showTransactions(transactions);
                        },
                        error -> {
                            error.printStackTrace();
                            getMvpView().showError("loadTransactions", error);
                        },
                        () -> Timber.i("onCompleted loadTransactions")
                )
        );
    }
    
    /**
     * Add a new Transaction.
     */
    public void addTransaction(final Transaction newTransaction, long categoryId) {
        mDisposables.add(
                mDataManager.addTransaction(newTransaction, categoryId)
                .subscribeOn(mBackgroundSchedulers)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        val -> Timber.i("addTransaction onNext"),
                        error -> {
                            error.printStackTrace();
                            getMvpView().showError("addTransaction", error);
                        },
                        () -> {
                            getMvpView().setBalanceTextView(mDataManager.getPreferencesHelper()
                                                .updateBalance(newTransaction.getAmount()));
                            getMvpView().onCompleteAddTransactions();
                            Timber.i("addTransaction onCompleted");
                        }
                )
        );
    }
    
    /**
     * Update the Transaction with TransactionId.
     */
    public void updateTransaction(long TransactionId, Transaction_Fields[] key, Object[] value) {
        mDisposables.add(
                mDataManager.updateTransaction(TransactionId, key, value)
                        .subscribeOn(mBackgroundSchedulers)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                val -> {},
                                error -> {
                                    error.printStackTrace();
                                    getMvpView().showError("updateTransaction", error);
                                },
                                () -> getMvpView().updateTransactions()
                        )
        );
    }
    
    /**
     * Load categories from Database.
     */
    public void loadCategories() {
        mDisposables.add(
                mDataManager.getCategories()
                .subscribeOn(mBackgroundSchedulers)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> getMvpView().showCategories(categories),
                        error -> {
                            error.printStackTrace();
                            getMvpView().showError("loadCategories", error);
                        },
                        () -> Timber.i("addTransaction onCompleted")
                )
        );
    }
    /**
     * Add a new category.
     */
    public void addCategory(final Category newCategory) {
        mDisposables.add(
                mDataManager.addCategory(newCategory)
                .subscribeOn(mBackgroundSchedulers)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        val -> Timber.i("addCategory onNext"),
                        error -> {
                            error.printStackTrace();
                            getMvpView().showError("addCategory", error);
                        },
                        () -> {
                            Timber.i("addCategory onCompleted");
                            getMvpView().addCategoryCompleted();
                        }
                )
        );
    }
    /**
     * Search the db for the specify date.
     * @param isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
     */
    public void searchTransactionWithDate(int day, int month, int year,
                                          int isDailyOrMonthlyOrYearly) {
        mDisposables.add(
                mDataManager.searchTransactionWithDate(day, month, year, isDailyOrMonthlyOrYearly)
                .subscribeOn(mBackgroundSchedulers)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactions -> {
                            getMvpView().showTransactions(transactions);
                        },
                        error -> {
                            error.printStackTrace();
                            getMvpView().showError("searchTransactionWithDate", error);
                        },
                        () -> Timber.i("searchTransactionWithDate onCompleted")
                )
        );

    }

    /**
     *
     * @param selectedIds : ids selected to be removed from db or the api.
     */
    public void deleteTransactions(long[] selectedIds) {

        mDisposables.add(
                mDataManager
                        .deleteTransactions(selectedIds)
                        .subscribeOn(mBackgroundSchedulers)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                transactionResponse -> {
                                    Timber.i("deleteTransactions onNext");
                                    float amount = transactionResponse.getAmountCount();
                                    mDataManager.getPreferencesHelper().setUserMoney(amount);
                                    getMvpView().setBalanceTextView(amount);
                                    getMvpView().deleteTransactionsComplete(amount==0);
                                },
                                error -> {
                                    error.printStackTrace();
                                    Timber.i("deleteTransactions onError");
                                    getMvpView().showErrorDeleteTransactions();
                                },
                                () -> {}
                        )
        );
    }

    /**
     * Remove the link of attachment to transaction
     * In {@link DataManager} it checks for sync or non-sync
     * @param transactionId: the id of transaction to remove the attachment.
     */
    void removeAttachmentFromDB(long transactionId) {
        Transaction_Fields[] key = new Transaction_Fields[] {Transaction_Fields.ATTACHMENT};
        updateTransaction(transactionId, key, new Object[] {null});
    }
}
