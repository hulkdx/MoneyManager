package com.hulkdx.moneymanagerv2.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.ui.base.BaseFragment
import com.hulkdx.moneymanagerv2.utils.WrongArgumentException

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
class LoginFragment: BaseFragment<TutorialActivity>() {

    private var isSync = false

    //---------------------------------------------------------------
    // statics
    //---------------------------------------------------------------

    companion object {
        private const val ARG_LOGIN_TYPE = "ARG_LOGIN_TYPE"

        fun newInstance(isSync: Boolean): LoginFragment {
            val fragment = LoginFragment()

            val args = Bundle()
            args.putBoolean(ARG_LOGIN_TYPE, isSync)
            fragment.arguments = args

            return fragment
        }
    }

    //---------------------------------------------------------------
    // Lifecycle
    //---------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAllArguments()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(
                if (isSync) R.layout.tutorial_fragment_login else R.layout.tutorial_fragment_login,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    //---------------------------------------------------------------
    // Arguments
    //---------------------------------------------------------------

    private fun getAllArguments() {
        val arg = arguments ?: throw WrongArgumentException()
        isSync = arg.getBoolean(ARG_LOGIN_TYPE)
    }

    //---------------------------------------------------------------
    // Override
    //---------------------------------------------------------------

}

