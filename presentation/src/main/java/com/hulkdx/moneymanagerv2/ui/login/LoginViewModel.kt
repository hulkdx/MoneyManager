package com.hulkdx.moneymanagerv2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hulkdx.com.domain.usecase.AuthUseCase
import hulkdx.com.domain.usecase.AuthUseCase.LoginResult
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class LoginViewModel @Inject constructor(
        private val mAuthUseCase: AuthUseCase
): ViewModel() {

    private var mLoginResult = MutableLiveData<LoginResult>()

    fun getLoginResult(): LiveData<LoginResult> = mLoginResult

    override fun onCleared() {
        super.onCleared()
        mAuthUseCase.dispose()
    }

    fun login(username: String, password: String) {
        mAuthUseCase.loginAsync(username, password, onComplete = {
            mLoginResult.value = it
        })
    }

}