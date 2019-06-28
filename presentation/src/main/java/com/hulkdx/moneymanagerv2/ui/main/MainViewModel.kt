package com.hulkdx.moneymanagerv2.ui.main

import androidx.lifecycle.ViewModel
import hulkdx.com.domain.usecase.AuthUseCase
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class MainViewModel @Inject constructor(
        private val mAuthUseCase: AuthUseCase
): ViewModel() {

    fun isLoggedIn(): Boolean {
        return mAuthUseCase.isLoggedIn()
    }
}