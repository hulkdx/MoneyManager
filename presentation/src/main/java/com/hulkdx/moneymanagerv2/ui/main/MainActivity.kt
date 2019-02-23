package com.hulkdx.moneymanagerv2.ui.main

import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.ui.base.BaseActivity

import android.os.Bundle
import com.hulkdx.moneymanagerv2.ViewModelProviderFactory

import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject lateinit var mViewModelProviderFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActivityComponent.inject(this)

        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, MainFragment())
        }
    }
}
