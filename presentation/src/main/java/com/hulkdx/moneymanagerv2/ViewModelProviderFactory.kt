package com.hulkdx.moneymanagerv2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hulkdx.com.domain.di.PerActivity
import com.hulkdx.moneymanagerv2.ui.auth.AuthViewModel
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/02/2019.
 */
@PerActivity
class ViewModelProviderFactory @Inject constructor() : ViewModelProvider.Factory {

    @Inject lateinit var mAuthViewModel: AuthViewModel

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == AuthViewModel::class.java) {
            return mAuthViewModel as T
        }

        throw RuntimeException("Please add other ViewModels")
    }
}
