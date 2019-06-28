package com.hulkdx.moneymanagerv2.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.ui.transaction.TransactionFragmentList
import com.hulkdx.moneymanagerv2.ui.welcome.WelcomeFragment
import com.hulkdx.moneymanagerv2.util.addFragment
import com.hulkdx.moneymanagerv2.util.getViewModel

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class MainActivity: AppCompatActivity() {

    private lateinit var mMainViewModel: MainViewModel

    // region Lifecycle ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        mMainViewModel = getViewModel()

        configureFragments(savedInstanceState == null)
    }

    // endregion Lifecycle -------------------------------------------------------------------------
    // region Fragment -----------------------------------------------------------------------------

    private fun configureFragments(isFirstTime: Boolean) {
        if (isFirstTime) {
            if (mMainViewModel.isLoggedIn()) {
                addFragment(R.id.container, TransactionFragmentList())
            } else {
                addFragment(R.id.container, WelcomeFragment())
            }
        }
    }

    // endregion Fragment---------------------------------------------------------------------------

}
