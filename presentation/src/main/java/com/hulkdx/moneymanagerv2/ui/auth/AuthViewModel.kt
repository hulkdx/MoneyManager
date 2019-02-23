package com.hulkdx.moneymanagerv2.ui.auth

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hulkdx.moneymanagerv2.di.ConfigPersistent
import hulkdx.com.domain.interactor.AuthUseCase

import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@SuppressLint("Registered")
@ConfigPersistent
class AuthViewModel @Inject constructor(val mAuthUseCase: AuthUseCase): ViewModel()  {

    private val mIsUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun isUserLoggedIn(): LiveData<Boolean> {
        if (mIsUserLoggedIn.value == null) {
            // TODO
        }
        return mIsUserLoggedIn
    }

    fun isUserLoggedInSync(): Boolean {
        return mAuthUseCase.isLoggedIn()
    }

}
