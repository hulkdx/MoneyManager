package com.hulkdx.moneymanagerv2.ui.tutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.util.addFragment

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class TutorialActivity : AppCompatActivity() {

    // region Lifecycle ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        configureFragments(savedInstanceState == null)
    }

    // endregion Lifecycle -------------------------------------------------------------------------
    // region Fragment -----------------------------------------------------------------------------

    private fun configureFragments(isFirstTime: Boolean) {
        if (isFirstTime) {
            addFragment(R.id.container, WelcomeFragment())
        }
    }
    // endregion Fragment
    //---------------------------------------------------------------------------------------------
}
