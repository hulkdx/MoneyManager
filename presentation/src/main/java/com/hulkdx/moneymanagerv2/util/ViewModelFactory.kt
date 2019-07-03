package com.hulkdx.moneymanagerv2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hulkdx.moneymanagerv2.ui.login.LoginViewModel
import com.hulkdx.moneymanagerv2.ui.main.MainViewModel
import com.hulkdx.moneymanagerv2.ui.register.RegisterViewModel
import com.hulkdx.moneymanagerv2.ui.transaction.TransactionViewModel
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class ViewModelFactory @Inject constructor(
        private val mLoginViewModel: LoginViewModel,
        private val mRegisterViewModel: RegisterViewModel,
        private val mTransactionViewModel: TransactionViewModel,
        private val mMainViewModel: MainViewModel
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> mLoginViewModel as T
            RegisterViewModel::class.java -> mRegisterViewModel as T
            TransactionViewModel::class.java -> mTransactionViewModel as T
            MainViewModel::class.java -> mMainViewModel as T
            else -> throw RuntimeException("Please add other ViewModels")
        }
    }

}