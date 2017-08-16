/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */
package com.hulkdx.moneymanager.ui.login_sync;

import android.text.TextUtils;
import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.injection.ConfigPersistent;
import com.hulkdx.moneymanager.ui.base.BasePresenter;
import com.hulkdx.moneymanager.ui.login.LoginMvpView;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class LoginSyncPresenter extends BasePresenter<LoginSyncMvpView> {

    private final DataManager mDataManager;

    private Disposable mDisposable;

    @Inject
    public LoginSyncPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginSyncMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }
}
