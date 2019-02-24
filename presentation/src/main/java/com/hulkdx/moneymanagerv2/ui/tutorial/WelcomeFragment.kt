package com.hulkdx.moneymanagerv2.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.ui.base.BaseFragment
import kotlinx.android.synthetic.main.tutorial_fragment_welcome.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
class WelcomeFragment: BaseFragment<TutorialActivity>() {

    //---------------------------------------------------------------
    // Lifecycle
    //---------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    private fun setupViews() {
        btn_yes.setOnClickListener {
            getCastedActivity().replaceFragment(R.id.container, LoginFragment.newInstance(true))
        }
        btn_no.setOnClickListener {
            getCastedActivity().replaceFragment(R.id.container, LoginFragment.newInstance(false))
        }
    }
}

