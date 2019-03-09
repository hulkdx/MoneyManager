package com.hulkdx.moneymanagerv2.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hulkdx.com.domain.di.PerActivity
import com.hulkdx.moneymanagerv2.ui.auth.AuthViewModel
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/02/2019.
 */
@PerActivity
class ViewModelProviderFactory @Inject constructor(
        private val mAuthViewModel: AuthViewModel
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == AuthViewModel::class.java) {
            return mAuthViewModel as T
        }

        throw RuntimeException("Please add other ViewModels")
    }
}
