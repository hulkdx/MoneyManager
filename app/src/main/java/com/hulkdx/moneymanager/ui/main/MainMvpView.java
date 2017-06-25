package com.hulkdx.moneymanager.ui.main;

import java.util.List;

import com.hulkdx.moneymanager.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showNameError();
    void hideNameError();

    void hideInitialError();
    void showInitialError();

    void setEnabledButton(Boolean isValid);
}
