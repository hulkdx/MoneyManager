package com.hulkdx.moneymanagerv2.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.inject
import com.hulkdx.moneymanagerv2.util.getViewModel
import kotlinx.android.synthetic.main.tutorial_fragment_register.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
class RegisterFragment : Fragment() {

    private lateinit var mRegisterViewModel: RegisterViewModel

    // region Lifecycle ---------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRegisterViewModel = getViewModel()

        setupSpinner()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    // endregion Lifecycle -------------------------------------------------------------

    private fun setupSpinner() {
        val mCurrencyArray = arrayOf(
                getString(R.string.register_select_currency),
                "EUR",
                "USD",
                "GBP",
                "INR",
                "AUD",
                "CAD",
                "SGD",
                "CHF",
                "MYR",
                "JPY",
                "CNY")
        val currencySpinnerAdapter = ArrayAdapter<String>(context, R.layout.spinner_currency_fragment_register, mCurrencyArray)
        currencySpinner.adapter = currencySpinnerAdapter
    }

}
