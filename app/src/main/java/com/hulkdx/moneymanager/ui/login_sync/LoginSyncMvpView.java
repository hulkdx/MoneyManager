/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */
package com.hulkdx.moneymanager.ui.login_sync;

import com.hulkdx.moneymanager.ui.base.MvpView;

public interface LoginSyncMvpView extends MvpView {
    void showUserNameError();

    void hideUserNameError();

    void showPasswordError();

    void hidePasswordError();

    void setEnableLoginBtn(Boolean isValid);


    void showLoginError(String errorMessage);

    void successfullyLoggedIn();
}
