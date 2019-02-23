package com.hulkdx.moneymanagerv2.ui.base

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/02/2019.
 */

interface BaseContract {

    interface Presenter<T : View> {
        fun attach(view: T)
        fun detach()
    }

    interface View

}