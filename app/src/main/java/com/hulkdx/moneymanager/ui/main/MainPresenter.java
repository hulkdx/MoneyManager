/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import javax.inject.Inject;
import rx.Subscription;

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

    public void loadTransactions() {
        checkViewAttached();
        mSubscription = mDataManager.getTransactions()
//                .observeOn(AndroidSchedulers.mainThread())
                //.subscribeOn(Schedulers.io())
                // TODO
                .subscribe();
    }
}
