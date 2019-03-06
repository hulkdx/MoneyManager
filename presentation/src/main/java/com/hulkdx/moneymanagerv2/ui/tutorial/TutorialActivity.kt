package com.hulkdx.moneymanagerv2.ui.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
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
    // statics
    //---------------------------------------------------------------

    companion object {

        var currentStep = WelcomeFragment.STEP_ID

        fun startActivity(context: Context) {
            val intent = Intent()
            context.startActivity(intent)
        }
    }

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

        configureFragments(savedInstanceState == null)
    }

    override fun onDestroyWithoutConfigurationChange() {
        currentStep = 0
        super.onDestroyWithoutConfigurationChange()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        currentFragment?.let {
            setCurrentFragmentStep(it)
        }
    }

    //---------------------------------------------------------------
    // Fragments
    //---------------------------------------------------------------

    private fun configureFragments(isFirstTime: Boolean) {

        if (isFirstTime) {
            addFragment(R.id.container, WelcomeFragment())
        } else {
            when (currentStep) {
                WelcomeFragment.STEP_ID -> {
                    // is the state lost?
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
                    if (currentFragment == null) {
                        addFragment(R.id.container, WelcomeFragment())
                    }
                }
                LoginFragment.STEP_ID_SYNC ->
                    replaceFragment(R.id.container, LoginFragment.newInstance(true))
                LoginFragment.STEP_ID_NON_SYNC ->
                    replaceFragment(R.id.container, LoginFragment.newInstance(false))
            }
        }
    }

    override fun replaceFragment(containerViewId: Int, fragment: BaseFragment<*>) {
        setCurrentFragmentStep(fragment)
        super.replaceFragment(containerViewId, fragment)
    }

    private fun setCurrentFragmentStep(fragment: Fragment) {
        when (fragment) {
            is WelcomeFragment -> {
                currentStep = WelcomeFragment.STEP_ID
            }
            is LoginFragment -> {
                currentStep = if (fragment.isSync) LoginFragment.STEP_ID_SYNC
                              else LoginFragment.STEP_ID_NON_SYNC
            }
        }
    }
}
