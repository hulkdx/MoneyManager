package com.hulkdx.moneymanagerv2.ui.tutorial

import android.content.Intent
import android.os.Bundle

import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.ui.base.BaseActivity
import com.hulkdx.moneymanagerv2.ui.main.MainActivity

/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 * Updated on 23/2/2019.
 *
 * The entry point of the application.
 */
class TutorialActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)
        activityComponent.inject(this)

//        if (mDataManager!!.checkLoggedIn()) {
//            redirectToMainActivity()
//        }
    }

    private fun redirectToMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
//
//    @OnClick(R.id.btn_yes)
//    fun onClickYes() {
//        val intent = Intent(this, LoginSyncActivity::class.java)
//        startActivity(intent)
//    }
//
//    @OnClick(R.id.btn_no)
//    fun onClickNo() {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//    }
}
