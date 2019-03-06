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

    //---------------------------------------------------------------
    // statics
    //---------------------------------------------------------------

    companion object {
        private const val ARG_LOGIN_TYPE = "ARG_LOGIN_TYPE"

        const val STEP_ID_SYNC = 1
        const val STEP_ID_NON_SYNC = 2

        fun newInstance(isSync: Boolean): LoginFragment {
            val fragment = LoginFragment()

            val args = Bundle()
            args.putBoolean(ARG_LOGIN_TYPE, isSync)
            fragment.arguments = args

            return fragment
        }
    }

    //---------------------------------------------------------------
    // fields
    //---------------------------------------------------------------

    private val args: Bundle by lazy {
        return@lazy arguments ?: throw WrongArgumentException()
    }

    val isSync: Boolean by lazy {
        return@lazy args.getBoolean(ARG_LOGIN_TYPE)
    }

    //---------------------------------------------------------------
    // Lifecycle
    //---------------------------------------------------------------

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


    //---------------------------------------------------------------
    // Override
    //---------------------------------------------------------------

}

