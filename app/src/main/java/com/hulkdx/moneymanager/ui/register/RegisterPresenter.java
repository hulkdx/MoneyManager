/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/21/2017.
 */
package com.hulkdx.moneymanager.ui.register;

import android.text.TextUtils;
import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import com.hulkdx.moneymanager.util.JsonReader;
import java.util.regex.Pattern;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

@ConfigPersistent
public class RegisterPresenter extends BasePresenter<RegisterMvpView> {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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

    public void validation(Observable<CharSequence> username, Observable<CharSequence> password,
                           Observable<CharSequence> email, Observable<CharSequence> confirmEmail) {
        mDisposables.add(
                Observable.combineLatest(
                        username, password, email, confirmEmail,
                        (newName, newPassword, newEmail, newConfrimEmail) -> {
                            boolean nameValid = !TextUtils.isEmpty(newName);
                            if (!nameValid) { getMvpView().showUserNameError(); }
                            else { getMvpView().hideUserNameError(); }

                            boolean passwordValid = !TextUtils.isEmpty(newPassword);
                            if (!passwordValid) { getMvpView().showPasswordError(); }
                            else { getMvpView().hidePasswordError(); }

                            boolean emailValid = !TextUtils.isEmpty(newEmail);
                            boolean isNewEmailAnEmail = VALID_EMAIL_ADDRESS_REGEX.matcher(newEmail).find();
                            if (!emailValid) { getMvpView().showEmailError(0); }
                            else if (!isNewEmailAnEmail) { getMvpView().showEmailError(1); }
                            else { getMvpView().hideEmailError(); }

                            boolean confirmEmailValid = !TextUtils.isEmpty(newConfrimEmail);
                            boolean isNewConfEmailAnEmail = VALID_EMAIL_ADDRESS_REGEX.matcher(newEmail).find();
                            boolean emailEquals = newEmail.toString().equals(newConfrimEmail.toString());
                            if (!confirmEmailValid) { getMvpView().showConfirmEmailError(0); }
                            else if (!isNewEmailAnEmail) { getMvpView().showConfirmEmailError(1); }
                            else if (!emailEquals) { getMvpView().showConfirmEmailError(2); }
                            else { getMvpView().hideConfirmEmailError(); }

                            return nameValid && passwordValid && emailValid && confirmEmailValid &&
                                    emailEquals && isNewEmailAnEmail && isNewConfEmailAnEmail;
                        })
                        .distinctUntilChanged()
                        .subscribe(isValid -> getMvpView().setEnableLoginBtn(isValid))
        );

    }

    public void register(String username, String password, String email) {
         mDisposables.add(
            mDataManager.register(username, password, email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            user -> {
                                Timber.i("onNext");
                                // TODO! get the money and currency from api! SAVE TOKEN AND OTHER INFO
                                mDataManager.getPreferencesHelper().saveUserInformation(
                                        user.getUsername(), 0, "EUR");
//                                // Redirect to new screen.
                                getMvpView().successfullyRegistered();
                            },
                            error -> {
                                Timber.i("onError");
                                if (error instanceof HttpException){
                                    if (((HttpException) error).code() == 500) {
                                        getMvpView().showRegisterError( JsonReader.getErrorMessage(
                                                ((HttpException) error).response().errorBody()));
                                    }
                                } else {
                                    getMvpView().showRegisterError(error.toString());
                                }
                            },
                            () -> Timber.i("onComplete")
                    )
        );
    }
}
