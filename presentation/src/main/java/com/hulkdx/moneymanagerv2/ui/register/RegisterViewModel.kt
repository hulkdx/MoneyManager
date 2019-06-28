package com.hulkdx.moneymanagerv2.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hulkdx.com.domain.usecase.AuthUseCase
import hulkdx.com.domain.usecase.AuthUseCase.LoginResult
import hulkdx.com.domain.usecase.AuthUseCase.RegisterResult
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class RegisterViewModel @Inject constructor(
        private val mAuthUseCase: AuthUseCase
): ViewModel() {

    private var mRegisterResult = MutableLiveData<RegisterResult>()

    fun getRegisterResult(): LiveData<RegisterResult> = mRegisterResult

    override fun onCleared() {
        super.onCleared()
        mAuthUseCase.dispose()
    }

    fun register(firstName: String,
                 lastName:  String,
                 username:  String,
                 password:  String,
                 email:     String,
                 currency:  String) {

        mAuthUseCase.registerAsync(firstName, lastName, username, password, email, currency,
                onComplete = {
                    mRegisterResult.value = it
                })
    }
}