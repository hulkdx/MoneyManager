/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */
package com.hulkdx.moneymanager.ui.login_sync;

import android.text.TextUtils;
import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import com.hulkdx.moneymanager.util.JsonReader;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@ConfigPersistent
public class LoginSyncPresenter extends BasePresenter<LoginSyncMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mDisposables;

    @Inject
    public LoginSyncPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(LoginSyncMvpView mvpView) {
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
                    (newName, newPassword) -> {
                        boolean nameValid = !TextUtils.isEmpty(newName);
                        if (!nameValid) { getMvpView().showUserNameError(); }
                        else { getMvpView().hideUserNameError(); }
                        boolean passwordValid = !TextUtils.isEmpty(newPassword);
                        if (!passwordValid) { getMvpView().showPasswordError(); }
                        else { getMvpView().hidePasswordError(); }
                        return nameValid && passwordValid;
                    })
                    .distinctUntilChanged()
                    .subscribe(isValid -> getMvpView().setEnableLoginBtn(isValid))
        );

    }

    public void login(String username, String password) {
        mDisposables.add(
            mDataManager.login(username, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            user -> {
                                mDataManager.getPreferencesHelper().saveUserInformation(
                                        user.getUsername(),
                                        0, user.getCurrency());
                                mDataManager.getPreferencesHelper().saveToken(user.getToken());
                                mDataManager.getPreferencesHelper().saveSync(true);
                                // Redirect to new screen.
                                getMvpView().successfullyLoggedIn();
                            },
                            error -> {
                                if (error instanceof HttpException){
                                    // Username and password is invalid!
                                    if (((HttpException) error).code() == 500) {
                                        getMvpView().showLoginError( JsonReader.getErrorMessage(
                                                ((HttpException) error).response().errorBody()));
                                    }
                                } else {
                                    getMvpView().showLoginError(error.toString());
                                }
                                getMvpView().setEnableLoginBtn(true);
                            }
                    )
        );
    }
}
