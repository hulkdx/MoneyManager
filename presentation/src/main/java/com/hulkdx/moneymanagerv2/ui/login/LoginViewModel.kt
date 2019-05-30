package com.hulkdx.moneymanagerv2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hulkdx.com.domain.data.remote.RemoteStatus
import hulkdx.com.domain.usecase.LoginUseCase
import hulkdx.com.domain.usecase.LoginUseCase.LoginResult

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class LoginViewModel(
        private val mLoginUseCase: LoginUseCase
): ViewModel() {

    private var mLoginResult = MutableLiveData<LoginResult>()

    override fun onCleared() {
        super.onCleared()
        mLoginUseCase.dispose()
    }

    fun login(username: String, password: String) {
        mLoginUseCase.loginAsync(username, password, onComplete = {
            mLoginResult.value = it
        })
    }

    fun getUserLoggedIn(): LiveData<LoginResult> {
        return mLoginResult
    }
}