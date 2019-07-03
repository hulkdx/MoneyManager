package com.hulkdx.moneymanagerv2.di

import com.hulkdx.moneymanagerv2.applicationComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerLoginFragmentComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerMainActivityComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerRegisterFragmentComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerTransactionFragmentListComponent
import com.hulkdx.moneymanagerv2.ui.login.LoginFragment
import com.hulkdx.moneymanagerv2.ui.main.MainActivity
import com.hulkdx.moneymanagerv2.ui.register.RegisterFragment
import com.hulkdx.moneymanagerv2.ui.transaction.TransactionFragmentList

/**
 * Helper functions for injections.
 *
 * Created by Mohammad Jafarzadeh Rezvan on 01/06/2019.
 */

fun LoginFragment.inject() {
    DaggerLoginFragmentComponent.builder()
            .fragment(this)
            .context(requireContext())
            .applicationComponent(applicationComponent(requireContext()))
            .build()
            .inject(this)
}

fun RegisterFragment.inject() {
    DaggerRegisterFragmentComponent.builder()
            .fragment(this)
            .context(requireContext())
            .applicationComponent(applicationComponent(requireContext()))
            .build()
            .inject(this)
}

fun TransactionFragmentList.inject() {
    DaggerTransactionFragmentListComponent
            .builder()
            .fragment(this)
            .context(requireContext())
            .applicationComponent(applicationComponent(requireContext()))
            .build()
            .inject(this)
}

fun MainActivity.inject() {
    DaggerMainActivityComponent
            .builder()
            .context(this)
            .applicationComponent(applicationComponent(this))
            .build()
            .inject(this)
}
