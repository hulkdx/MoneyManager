package com.hulkdx.moneymanagerv2.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hulkdx.moneymanagerv2.BuildConfig
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.inject
import com.hulkdx.moneymanagerv2.util.getViewModel
import hulkdx.com.domain.data.model.SUPPORTED_CURRENCIES
import hulkdx.com.domain.data.remote.RegisterAuthError
import hulkdx.com.domain.data.remote.RemoteStatus
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
        setupRegisterBtn()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mRegisterViewModel.getRegisterResult().observe(this, Observer {

            if (BuildConfig.DEBUG) {
                it.throwable?.message?.apply {
                    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
                }
            }

            when (it.status) {
                RemoteStatus.SUCCESS        -> registerSuccess()
                RemoteStatus.AUTH_ERROR     -> {
                    when (it.authError) {
                        RegisterAuthError.EMAIL_EXISTS -> registerEmailAlreadyExists()
                    }
                }
                RemoteStatus.NETWORK_ERROR  -> registerNetworkError()
                RemoteStatus.GENERAL_ERROR  -> registerGeneralError()
            }
        })
    }

    // endregion Lifecycle -------------------------------------------------------------
    // region Setup Views -------------------------------------------------------------

    private fun setupSpinner() {
        val currencyArray = arrayOf(getString(R.string.register_select_currency)) +
                SUPPORTED_CURRENCIES
        val currencySpinnerAdapter = ArrayAdapter<String>(context,
                R.layout.spinner_currency_fragment_register, currencyArray)
        currencySpinner.adapter = currencySpinnerAdapter
    }

    private fun setupRegisterBtn() {
        registerBtn.setOnClickListener {
            val firstName   = firstNameEditText.text.toString()
            val lastName    = lastNameEditText.text.toString()
            val username    = usernameEditText.text.toString()
            val password    = passwordEditText.text.toString()
            val email       = emailEditText.text.toString()
            val currency    = currencySpinner.selectedItem.toString()

            mRegisterViewModel.register(firstName, lastName, username, password, email, currency)
        }
    }

    // endregion Setup Views -------------------------------------------------------------
    // region Register Callbacks -------------------------------------------------------------

    private fun registerGeneralError() {
        Toast.makeText(context, "registerGeneralError", Toast.LENGTH_LONG).show()
    }

    private fun registerNetworkError() {
        Toast.makeText(context, "registerNetworkError", Toast.LENGTH_LONG).show()
    }

    private fun registerSuccess() {
        Toast.makeText(context, "registerSuccess", Toast.LENGTH_LONG).show()
    }

    private fun registerEmailAlreadyExists() {
        Toast.makeText(context, "registerEmailAlreadyExists", Toast.LENGTH_LONG).show()
    }

    // endregion Register Callbacks -------------------------------------------------------------

}
