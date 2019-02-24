package com.hulkdx.moneymanagerv2.ui.auth

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hulkdx.com.domain.di.ConfigPersistent
import hulkdx.com.domain.interactor.AuthUseCase
import io.reactivex.functions.Consumer

import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@SuppressLint("Registered")
@ConfigPersistent
class AuthViewModel @Inject constructor(private val mAuthUseCase: AuthUseCase): ViewModel()  {

    private val mIsUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        mAuthUseCase.dispose()
    }

    fun userLoggedInLiveData(): LiveData<Boolean> {
        if (mIsUserLoggedIn.value == null) {
            isUserLoggedInAsync()
        }
        return mIsUserLoggedIn
    }

    private fun isUserLoggedInAsync() {
        mAuthUseCase.isLoggedInAsync(Consumer {
            mIsUserLoggedIn.value = it
        })
    }

    fun isUserLoggedInSync(): Boolean {
        return mAuthUseCase.isLoggedInSync()
    }

}
