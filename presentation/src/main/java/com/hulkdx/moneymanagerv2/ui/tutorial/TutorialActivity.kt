package com.hulkdx.moneymanagerv2.ui.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.ui.auth.AuthActivity
import com.hulkdx.moneymanagerv2.ui.base.BaseFragment
import timber.log.Timber

/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 * Updated on 23/2/2019.
 *
 * The entry point of the application.
 */
class TutorialActivity : AuthActivity() {

    //---------------------------------------------------------------
    // Lifecycle
    //---------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityComponent.inject(this)
        setContentView(R.layout.activity_tutorial)

        if (mAuthViewModel.isUserLoggedInSync()) {
            TODO("not implemented")
        }

        configureFragments()
    }

    //---------------------------------------------------------------
    // Fragments
    //---------------------------------------------------------------

    private fun configureFragments() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)

        if (currentFragment == null) {
            Timber.i("create fragment")
            addFragment(R.id.container, WelcomeFragment())
        }
    }

    //---------------------------------------------------------------
    // statics
    //---------------------------------------------------------------

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent()
            context.startActivity(intent)
        }
    }
}
