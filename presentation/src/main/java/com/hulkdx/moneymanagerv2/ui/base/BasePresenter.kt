package com.hulkdx.moneymanagerv2.ui.base

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
open class BasePresenter<T: BaseContract.View>: BaseContract.Presenter<T> {

    var view: T? = null
        private set

    override fun attach(view: T) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

}
