package com.hulkdx.moneymanagerv2.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hulkdx.com.domain.usecase.AuthUseCase.LoginResult
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class RegisterViewModel @Inject constructor(
): ViewModel() {

    private var mRegisterResult = MutableLiveData<LoginResult>()

    fun getRegisterResult(): LiveData<LoginResult> = mRegisterResult

    override fun onCleared() {
        super.onCleared()
    }

    fun register(firstName: String,
                 lastName: String,
                 username: String,
                 password: String,
                 email: String,
                 currency: String) {

    }
}