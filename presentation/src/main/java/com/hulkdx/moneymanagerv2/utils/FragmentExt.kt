package com.hulkdx.moneymanagerv2.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.hulkdx.moneymanagerv2.ui.base.BaseFragment

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/02/2019.
 */
inline fun <reified T: ViewModel> BaseFragment.getViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}