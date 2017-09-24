/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 */
package com.hulkdx.moneymanagerv2.ui.login;

import javax.inject.Inject;
import com.hulkdx.moneymanagerv2.data.DataManager;
import com.hulkdx.moneymanagerv2.injection.ConfigPersistent;
import com.hulkdx.moneymanagerv2.ui.base.BasePresenter;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginMvpView> {

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
    }

    public void saveUserInformation(String name, int initialMoney, String currency) {
        mDataManager.getPreferencesHelper().saveUserInformation(name, initialMoney, currency);
        mDataManager.getPreferencesHelper().saveSync(false);

    }
}
