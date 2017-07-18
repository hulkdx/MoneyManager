/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import com.hulkdx.moneymanager.util.RxUtil;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.Subscription;
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

    /*
    * Load transactions from DataManager.
     */
    public void loadTransactions() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getTransactions()
//                .observeOn(AndroidSchedulers.mainThread())
                //.subscribeOn(Schedulers.io())
                // TODO
                .subscribe(new Subscriber<List<Transaction>>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("onError");
                    }

                    @Override
                    public void onNext(List<Transaction> transactions) {
                        if (transactions.isEmpty()) {
                            getMvpView().showEmptyTransactions();
                        } else {
                            getMvpView().showTransactions(transactions);
                        }
                    }
                });
    }

    public void addTransaction(Transaction newTransaction) {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.addTransaction(newTransaction)
                .subscribe(new Subscriber<Transaction>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("addTransaction onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("addTransaction onError");
                    }

                    @Override
                    public void onNext(Transaction transaction) {
                        Timber.e("addTransaction onNext");
                    }
                });
    }
}
