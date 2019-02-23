package com.hulkdx.moneymanagerv2.ui.auth

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hulkdx.moneymanagerv2.di.ConfigPersistent

import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@SuppressLint("Registered")
@ConfigPersistent
class AuthViewModel @Inject constructor(): ViewModel()  {

    private val mIsUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun isUserLoggedIn(): LiveData<Boolean> {
        if (mIsUserLoggedIn.value == null) {
            // TODO
        }
        return mIsUserLoggedIn
    }

    fun isUserLoggedInSync(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
