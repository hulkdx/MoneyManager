/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/21/2017.
 */
package com.hulkdx.moneymanager.ui.register;

import com.hulkdx.moneymanager.ui.base.MvpView;

public interface RegisterMvpView extends MvpView {
    void showUserNameError();

    void hideUserNameError();

    void showPasswordError();

    void hidePasswordError();

    void setEnableLoginBtn(Boolean isValid);

}
