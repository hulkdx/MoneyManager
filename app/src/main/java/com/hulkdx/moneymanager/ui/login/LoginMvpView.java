/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 */
package com.hulkdx.moneymanager.ui.login;

import com.hulkdx.moneymanager.ui.base.MvpView;

public interface LoginMvpView extends MvpView {

    void showNameError();
    void hideNameError();

    void hideInitialError();
    void showInitialError();

    void setEnabledButton(Boolean isValid);
}
