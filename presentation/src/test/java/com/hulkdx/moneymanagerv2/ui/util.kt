package com.hulkdx.moneymanagerv2.ui

import org.mockito.ArgumentCaptor
import org.mockito.Mockito


fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
fun <T> anyKotlin(): T {
    Mockito.any<T>()
    return uninitialized()
}
private fun <T> uninitialized(): T = null as T