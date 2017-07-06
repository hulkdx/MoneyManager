package com.hulkdx.moneymanager.ui.main;

import android.text.TextUtils;

import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import com.hulkdx.moneymanager.util.RxUtil;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func2;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private Subscription mSubscription;

    @Inject
    public MainPresenter() {
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
    Validating name EditText and Initial Money EditText, it shows error if its not validate and hide
    the button.
 */
    public void validation(Observable<CharSequence> nameObservable, Observable<CharSequence> initialMoneyObservable) {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = Observable.combineLatest(
                nameObservable, initialMoneyObservable,
                new Func2<CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence newName, CharSequence newInitialMoney) {
                        boolean nameValid = !TextUtils.isEmpty(newName);
                        if (!nameValid) getMvpView().showNameError();
                        else getMvpView().hideNameError();
                        boolean initialValid = !TextUtils.isEmpty(newInitialMoney);
                        if (!initialValid) getMvpView().showInitialError();
                        else getMvpView().hideInitialError();
                        return nameValid && initialValid;
                    }
                })
                .distinctUntilChanged()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isValid) {
                        getMvpView().setEnabledButton(isValid);
                    }
                });

    }

}
