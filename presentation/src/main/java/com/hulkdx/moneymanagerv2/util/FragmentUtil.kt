package com.hulkdx.moneymanagerv2.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.hulkdx.moneymanagerv2.MoneyManagerApplication
import com.hulkdx.moneymanagerv2.applicationComponent

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */

inline fun <reified VM: ViewModel> Fragment.getViewModel(): VM {
    val viewModelFactory = applicationComponent(context!!).viewModelFactory()
    return ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
}

fun Fragment.replaceFragment(container: Int, fragment: Fragment) {
    activity?.replaceFragment(container, fragment)

    if (activity == null) {
    }
}

fun FragmentActivity.addFragment(container: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
            .add(container, fragment)
            .commitAllowingStateLoss()
}

fun FragmentActivity.replaceFragment(container: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
            .replace(container, fragment)
            .commitAllowingStateLoss()
}