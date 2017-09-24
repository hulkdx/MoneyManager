/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/21/2017.
 */
package com.hulkdx.moneymanagerv2.ui.register;

import com.hulkdx.moneymanagerv2.data.DataManager;
import com.hulkdx.moneymanagerv2.injection.ConfigPersistent;
import com.hulkdx.moneymanagerv2.ui.base.BasePresenter;
import com.hulkdx.moneymanagerv2.util.JsonReader;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

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

    public void register(String username, String password, String email, String currency) {
        mDisposables.add(
                mDataManager.register(username, password, email, currency)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            user -> {
                                Timber.i("onNext");
                                // Show Register is complete, login now.
                                getMvpView().successfullyRegistered(user.getUsername());
                                getMvpView().setEnableRegisterBtn(true);
                            },
                            error -> {
                                Timber.i("onError");
                                if (error instanceof HttpException) {
                                    int errorCode = ((HttpException) error).code();

                                    if (errorCode == 500 || errorCode == 400) {
                                        getMvpView().showRegisterError( JsonReader.getErrorMessage(
                                                ((HttpException) error).response().errorBody()));
                                    } else {
                                        getMvpView().showRegisterError(error.toString());
                                    }
                                } else {
                                    getMvpView().showRegisterError(error.toString());
                                }
                                getMvpView().setEnableRegisterBtn(true);
                            },
                            () -> Timber.i("onComplete")
                    )
        );
    }
}
