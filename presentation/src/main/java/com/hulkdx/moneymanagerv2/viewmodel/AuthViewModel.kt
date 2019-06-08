package com.hulkdx.moneymanagerv2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hulkdx.com.domain.data.remote.RemoteStatus
import hulkdx.com.domain.usecase.LoginUseCase
import hulkdx.com.domain.usecase.LoginUseCase.LoginResult
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class AuthViewModel @Inject constructor(
        private val mLoginUseCase: LoginUseCase
): ViewModel() {

    private var mLoginResult = MutableLiveData<LoginResult>()

    fun login(username: String, password: String) {
        mLoginUseCase.loginAsync(username, password, onComplete = {
            mLoginResult.value = it
        })
    }

    fun register() {
        TODO()
    }

    // region helper methods -----------------------------------------------------------------------

    override fun onCleared() {
        super.onCleared()
        mLoginUseCase.dispose()
    }

    fun getLoginResult(): LiveData<LoginResult> {
        return mLoginResult
    }

    // endregion helper methods -----------------------------------------------------------------------

}