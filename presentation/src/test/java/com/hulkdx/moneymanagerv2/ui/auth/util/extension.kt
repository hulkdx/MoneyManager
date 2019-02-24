package com.hulkdx.moneymanagerv2.ui.auth.util

import org.mockito.Mockito

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */

fun <T> anyCustom(): T {
    Mockito.any<T>()
    return uninitialized()
}
private fun <T> uninitialized(): T = null as T