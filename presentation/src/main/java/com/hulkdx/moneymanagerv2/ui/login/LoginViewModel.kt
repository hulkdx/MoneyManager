package com.hulkdx.moneymanagerv2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class LoginViewModel: ViewModel() {

    private var mIsUserLoggedIn = MutableLiveData<Boolean>()

    fun login(userName: String, password: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getUserLoggedIn(): LiveData<Boolean> {
        return mIsUserLoggedIn
    }
}