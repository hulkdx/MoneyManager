package com.hulkdx.moneymanagerv2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hulkdx.moneymanagerv2.ui.login.LoginViewModel
import hulkdx.com.domain.usecase.LoginUseCase
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class ViewModelFactory @Inject constructor(
        private val mLoginUseCase: LoginUseCase
): ViewModelProvider.Factory
{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == LoginViewModel::class.java) {
            return LoginViewModel(mLoginUseCase) as T
        }
        throw RuntimeException("Please add other ViewModels")
    }

}