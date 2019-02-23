package com.hulkdx.moneymanagerv2.ui.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_chooser.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 * Updated on 23/2/2019.
 *
 * The entry point of the application.
 */
class TutorialActivity : AuthActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityComponent.inject(this)

        if (mAuthViewModel.isUserLoggedInSync()) {
            return
        }

        setContentView(R.layout.activity_chooser)

        btn_yes.setOnClickListener {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        btn_no.setOnClickListener {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent()
            context.startActivity(intent)
        }
    }
}
