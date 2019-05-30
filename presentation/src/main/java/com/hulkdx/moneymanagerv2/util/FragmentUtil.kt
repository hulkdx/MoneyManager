package com.hulkdx.moneymanagerv2.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
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