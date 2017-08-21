/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/21/2017.
 */
package com.hulkdx.moneymanager.ui.register;

import android.text.TextUtils;

import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import com.hulkdx.moneymanager.ui.login_sync.LoginSyncMvpView;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@ConfigPersistent
public class RegisterPresenter extends BasePresenter<RegisterMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mDisposables;

    @Inject
    public RegisterPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(RegisterMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposables != null) mDisposables.clear();
    }

    public void validation(Observable<CharSequence> username, Observable<CharSequence> password) {
        mDisposables.add(
                Observable.combineLatest(
                        username, password,
                        (newName, newInitialMoney) -> {
                            boolean nameValid = !TextUtils.isEmpty(newName);
                            if (!nameValid) { getMvpView().showUserNameError(); }
                            else { getMvpView().hideUserNameError(); }
                            boolean initialValid = !TextUtils.isEmpty(newInitialMoney);
                            if (!initialValid) { getMvpView().showPasswordError(); }
                            else { getMvpView().hidePasswordError(); }
                            return nameValid && initialValid;
                        })
                        .distinctUntilChanged()
                        .subscribe(isValid -> getMvpView().setEnableLoginBtn(isValid))
        );

    }

    public void register(String username, String password) {
//  TODO       mDisposables.add(
//            mDataManager.login(username, password)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(
//                            user -> {
//                                // TODO! get the money and currency from api! SAVE TOKEN AND OTHER INFO
//                                mDataManager.getPreferencesHelper().saveUserInformation(
//                                        user.getUsername(), 0, "EUR");
//                                // Redirect to new screen.
//                                getMvpView().successfullyLoggedIn();
//                            },
//                            error -> {
//                                if (error instanceof HttpException){
//                                    // Username and password is invalid!
//                                    if (((HttpException) error).code() == 500) {
//                                        getMvpView().showLoginError(getErrorMessage(
//                                                ((HttpException) error).response().errorBody()
//                                        ));
//                                    }
//                                } else {
//                                    getMvpView().showLoginError(error.toString());
//                                }
//                            }
//                    )
//        );
    }
}
