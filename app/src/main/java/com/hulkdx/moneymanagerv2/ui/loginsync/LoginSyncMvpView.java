/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */
package com.hulkdx.moneymanagerv2.ui.loginsync;

import com.hulkdx.moneymanagerv2.ui.base.MvpView;

public interface LoginSyncMvpView extends MvpView {

    void setEnableLoginBtn(Boolean isValid);

    void showLoginError(String errorMessage);

    void successfullyLoggedIn();
}
