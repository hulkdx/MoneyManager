/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private CompositeDisposable mDisposables;
    private final DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposables != null) mDisposables.clear();
    }

    public String getCurrencyName() {
        return mDataManager.getPreferencesHelper().getCurrencyName();
    }
    /*
    * Load transactions from DataManager.
     */
    public void loadTransactions() {
        getMvpView().setBalanceTextView(mDataManager.getPreferencesHelper().getUserMoney());
        mDisposables.add(
                mDataManager.getTransactions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactions -> {
                            if (transactions.isEmpty()) {
                            getMvpView().showEmptyTransactions(transactions);
                            } else {
                            getMvpView().showTransactions(transactions);
                            }
                        },
                        error -> getMvpView().showError("loadTransactions", error),
                        () -> Timber.i("onCompleted")
                )
        );
    }

    /*
    * Add a new Transaction.
     */
    public void addTransaction(final Transaction newTransaction, long categoryId) {
        mDisposables.add(
                mDataManager.addTransaction(newTransaction, categoryId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        val -> Timber.i("addTransaction onNext"),
                        error -> getMvpView().showError("addTransaction", error),
                        () -> {
                            getMvpView().setBalanceTextView(mDataManager.getPreferencesHelper()
                                                .updateBalance(newTransaction.getAmount()));
                            getMvpView().onCompleteAddTransactions();
                            Timber.i("addTransaction onCompleted");
                        }
                )
        );
    }

    public void loadCategories() {
        mDisposables.add(
                mDataManager.getCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> getMvpView().showCategories(categories),
                        error -> getMvpView().showError("loadCategories", error),
                        () -> Timber.i("addTransaction onCompleted")
                )
        );
    }

    public void addCategory(final Category newCategory) {
        mDisposables.add(
                mDataManager.addCategory(newCategory)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        val -> Timber.i("addCategory onNext"),
                        error -> getMvpView().showError("addCategory", error),
                        () -> {
                            Timber.i("addCategory onCompleted");
                            getMvpView().addCategoryDataSet(newCategory);
                        }
                )
        );
    }

    /*
     * Search the db for the specify date.
     * @param isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
     */
    public void searchTransactionWithDate(int day, int month, int year, int isDailyOrMonthlyOrYearly) {
        mDisposables.add(
                mDataManager.searchTransactionWithDate(day, month, year, isDailyOrMonthlyOrYearly)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactions -> {
                            getMvpView().showTransactions(transactions);
                        },
                        error -> getMvpView().showError("searchTransactionWithDate", error),
                        () -> Timber.i("searchTransactionWithDate onCompleted")
                )
        );

    }
}
