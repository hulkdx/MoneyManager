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
import com.hulkdx.moneymanagerv2.ui.login.LoginFragment
import com.hulkdx.moneymanagerv2.util.ViewModelFactory
import com.hulkdx.moneymanagerv2.util.getViewModel
import com.hulkdx.moneymanagerv2.util.replaceFragment
import hulkdx.com.domain.data.remote.RegisterAuthErrorStatus
import hulkdx.com.domain.usecase.AuthUseCase
import hulkdx.com.domain.util.SUPPORTED_CURRENCIES
import kotlinx.android.synthetic.main.tutorial_fragment_register.*
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 *
 * TODO add a progressbar and animations.
 */
class RegisterFragment : Fragment() {

    private lateinit var mRegisterViewModel: RegisterViewModel
    @Inject lateinit var mViewModelFactory: ViewModelFactory


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
        mRegisterViewModel = getViewModel(mViewModelFactory)

        setupSpinner()
        setupRegisterBtn()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mRegisterViewModel.getRegisterResult().observe(this, Observer { registerResult ->

            when (registerResult) {
                is AuthUseCase.RegisterResult.Successful -> registerSuccess()
                is AuthUseCase.RegisterResult.AuthError -> {
                    when (registerResult.status) {
                        RegisterAuthErrorStatus.EMAIL_EXISTS -> registerEmailAlreadyExists()
                        RegisterAuthErrorStatus.USER_EXISTS  -> registerUserAlreadyExists()
                    }
                }
                is AuthUseCase.RegisterResult.NetworkError -> {
                    showDebugRegisterResult(registerResult.throwable)
                    registerNetworkError()
                }
                is AuthUseCase.RegisterResult.GeneralError -> {
                    showDebugRegisterResult(registerResult.throwable)
                    registerGeneralError()
                }
            }
        })
    }

    // endregion Lifecycle -------------------------------------------------------------
    // region Setup Views -------------------------------------------------------------

    private fun setupSpinner() {
        val currencyArray = arrayOf(getString(R.string.register_select_currency)) +
                SUPPORTED_CURRENCIES
        val currencySpinnerAdapter = ArrayAdapter<String>(context!!,
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

            registerLoading()
            mRegisterViewModel.register(firstName, lastName, username, password, email, currency)
        }
    }

    // endregion Setup Views -------------------------------------------------------------
    // region Register Callbacks -------------------------------------------------------------

    private fun registerGeneralError() {
        val message = getString(R.string.general_error)
        registerShowError(message)
    }

    private fun registerNetworkError() {
        val message = getString(R.string.network_error)
        registerShowError(message)
    }

    private fun registerEmailAlreadyExists() {
        val message = getString(R.string.register_error_email_exists)
        registerShowError(message)
    }

    private fun registerUserAlreadyExists() {
        val message = getString(R.string.register_error_user_exists)
        registerShowError(message)
    }

    private fun registerLoading() {
        errorTextView.visibility = View.GONE
    }

    private fun registerSuccess() {
        replaceFragment(R.id.container, LoginFragment())
    }

    private fun registerShowError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }

    // endregion Register Callbacks -------------------------------------------------------------
    // region Extra -------------------------------------------------------------

    private fun showDebugRegisterResult(throwable: Throwable?) {
        if (BuildConfig.DEBUG && throwable != null && throwable.message != null) {
            Toast.makeText(context, throwable.message, Toast.LENGTH_LONG).show()
        }
    }

    // endregion Extra -------------------------------------------------------------

}
