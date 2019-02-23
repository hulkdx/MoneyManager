package com.hulkdx.moneymanagerv2.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.hulkdx.moneymanagerv2.ui.base.BaseActivity
import com.hulkdx.moneymanagerv2.ui.tutorial.TutorialActivity
import timber.log.Timber

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 *
 * Handling authentications
 */
@SuppressLint("Registered")
open class AuthActivity : BaseActivity() {

    lateinit var mAuthViewModel: AuthViewModel
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuthViewModel = ViewModelProviders.of(this, mViewModelProviderFactory).get(AuthViewModel::class.java)

        observeUserLogging()
    }

    private fun observeUserLogging() {

        if (this is TutorialActivity) return

        mAuthViewModel.isUserLoggedIn().observe(this, Observer {
            Timber.i("mIsUserLoggedIn $it")
            if (!it) {
                TutorialActivity.startActivity(this)
                finish()
            }
        })
    }
}
