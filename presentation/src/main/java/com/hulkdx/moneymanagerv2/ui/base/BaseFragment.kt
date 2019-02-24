package com.hulkdx.moneymanagerv2.ui.base

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
abstract class BaseFragment<ACTIVITY: BaseActivity>: Fragment() {
    init {
        retainInstance = true
    }

    @NonNull
    @Suppress("UNCHECKED_CAST")
    protected fun getCastedActivity(): ACTIVITY {
        return this.activity as ACTIVITY? ?: throw RuntimeException("cannot cast activity")
    }
}
