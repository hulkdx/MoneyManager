package com.hulkdx.moneymanagerv2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hulkdx.moneymanagerv2.ui.login.LoginViewModel
import com.hulkdx.moneymanagerv2.ui.register.RegisterViewModel
import hulkdx.com.domain.usecase.LoginUseCase
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class ViewModelFactory @Inject constructor(
        private val mLoginViewModel: LoginViewModel,
        private val mRegisterViewModel: RegisterViewModel
): ViewModelProvider.Factory
{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> mLoginViewModel as T
            RegisterViewModel::class.java -> mRegisterViewModel as T
            else -> throw RuntimeException("Please add other ViewModels")
        }
    }

}