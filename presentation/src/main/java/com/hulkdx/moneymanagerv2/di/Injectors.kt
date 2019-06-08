package com.hulkdx.moneymanagerv2.di

import com.hulkdx.moneymanagerv2.applicationComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerLoginFragmentComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerRegisterFragmentComponent
import com.hulkdx.moneymanagerv2.ui.tutorial.LoginFragment
import com.hulkdx.moneymanagerv2.ui.tutorial.RegisterFragment

/**
 * Created by Mohammad Jafarzadeh Rezvan on 01/06/2019.
 */
fun LoginFragment.inject() {
    DaggerLoginFragmentComponent.builder()
            .fragment(this)
            .applicationComponent(applicationComponent(requireContext()))
            .build()
            .inject(this)
}

fun RegisterFragment.inject() {
    DaggerRegisterFragmentComponent.builder()
            .fragment(this)
            .applicationComponent(applicationComponent(requireContext()))
            .build()
            .inject(this)
}