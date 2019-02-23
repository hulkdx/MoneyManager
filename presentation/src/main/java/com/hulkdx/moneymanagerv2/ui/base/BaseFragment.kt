package com.hulkdx.moneymanagerv2.ui.base

import androidx.fragment.app.Fragment

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
abstract class BaseFragment : Fragment() {
    init {
        retainInstance = true
    }
}
