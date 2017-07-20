/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import com.hulkdx.moneymanager.util.RxUtil;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private Subscription mSubscription;
    private DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public String getCurrencyName() {
        return mDataManager.getPreferencesHelper().getCurrencyName();
    }
    /*
    * Load transactions from DataManager.
     */
    public void loadTransactions() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        getMvpView().setBalanceTextView(mDataManager.getPreferencesHelper().getUserMoney());
        mSubscription = mDataManager.getTransactions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Transaction>>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("onError"  + e.toString());
                    }

                    @Override
                    public void onNext(List<Transaction> transactions) {
                        if (transactions.isEmpty()) {
                            getMvpView().showEmptyTransactions(transactions);
                        } else {
                            getMvpView().showTransactions(transactions);
                        }
                    }
                });
    }

    /*
    * Add a new Transaction.
     */
    public void addTransaction(final Transaction newTransaction) {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.addTransaction(newTransaction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Transaction>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().setBalanceTextView(mDataManager.getPreferencesHelper()
                                                .updateBalance(newTransaction.getAmount()));
                        Timber.i("addTransaction onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("addTransaction onError"  + e.toString()) ;
                    }

                    @Override
                    public void onNext(Transaction transaction) {
                        Timber.e("addTransaction onNext");
                    }
                });
    }

    public void loadCategories() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Category>>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("onError" + e.toString());
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        getMvpView().showCategories(categories);
                        Timber.i("size = " + categories.size());
                    }
                });
    }

    public void addCategory(final Category newCategory) {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.addCategory(newCategory)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Category>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("addCategory onCompleted");
                        getMvpView().addCategoryDataSet(newCategory);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("addCategory onError" + e.toString());
                    }

                    @Override
                    public void onNext(Category category) {
                        Timber.e("addCategory onNext");
                    }
                });
    }
}
