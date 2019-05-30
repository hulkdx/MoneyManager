package com.hulkdx.moneymanagerv2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hulkdx.com.domain.usecase.LoginUseCase
import hulkdx.com.domain.usecase.LoginUseCase.Companion.LOGIN_RESULT_SUCCESS

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class LoginViewModel(
        private val mLoginUseCase: LoginUseCase
): ViewModel() {

    private var mIsUserLoggedIn = MutableLiveData<Boolean>()

    fun login(username: String, password: String) {
        mLoginUseCase.loginAsync(username, password, onComplete = {
            mIsUserLoggedIn.value = it.status == LOGIN_RESULT_SUCCESS
        })
    }

    fun getUserLoggedIn(): LiveData<Boolean> {
        return mIsUserLoggedIn
    }
}