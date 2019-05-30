package com.hulkdx.moneymanagerv2.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.R

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
class LoginFragment : Fragment() {

    // region Lifecycle ---------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_fragment_login, container, false)
    }

    // endregion Lifecycle -------------------------------------------------------------

}

