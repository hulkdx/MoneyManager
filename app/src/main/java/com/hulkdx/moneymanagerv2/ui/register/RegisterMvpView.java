/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/21/2017.
 */
package com.hulkdx.moneymanagerv2.ui.register;

import com.hulkdx.moneymanagerv2.ui.base.MvpView;

public interface RegisterMvpView extends MvpView {

    void setEnableRegisterBtn(Boolean isValid);

    void successfullyRegistered(String username);

    void showRegisterError(String errorMessage);
}
