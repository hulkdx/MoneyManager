package com.hulkdx.moneymanagerv2.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.util.replaceFragment
import kotlinx.android.synthetic.main.tutorial_fragment_welcome.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
class WelcomeFragment: Fragment() {

    // region Lifecycle ---------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    // endregion Lifecycle ------------------------------------------------------------

    private fun setupViews() {
        btn_yes.setOnClickListener {
            replaceFragment(R.id.container, LoginFragment.newInstance(isSync = true))
        }
        btn_no.setOnClickListener {
            replaceFragment(R.id.container, LoginFragment.newInstance(isSync = false))
        }
    }
}

