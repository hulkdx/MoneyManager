/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 */
package com.hulkdx.moneymanager.ui.login;

import android.text.TextUtils;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginMvpView> {

    private Disposable mDisposable;
    private final DataManager mDataManager;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

/*
    Validating name EditText and Initial Money EditText, it shows error if its not validate and hide
    the button.
 */
    public void validation(Observable<CharSequence> nameObservable, Observable<CharSequence> initialMoneyObservable) {
        mDisposable = Observable.combineLatest(
                nameObservable, initialMoneyObservable,
                (newName, newInitialMoney) -> {
                    boolean nameValid = !TextUtils.isEmpty(newName);
                    if (!nameValid) { getMvpView().showNameError(); }
                    else { getMvpView().hideNameError(); }
                    boolean initialValid = !TextUtils.isEmpty(newInitialMoney);
                    if (!initialValid) { getMvpView().showInitialError(); }
                    else { getMvpView().hideInitialError(); }
                    return nameValid && initialValid;
                })
                .distinctUntilChanged()
                .subscribe(isValid -> getMvpView().setEnabledButton(isValid));

    }
    /*
    Check if the user logged in.
     */
    public boolean checkLoggedIn() {
        return mDataManager.checkLoggedIn();
    }

    public void saveUserInformation(String name, int initialMoney, String currency) {
        mDataManager.getPreferencesHelper().saveUserInformation(name, initialMoney, currency);

    }
}
