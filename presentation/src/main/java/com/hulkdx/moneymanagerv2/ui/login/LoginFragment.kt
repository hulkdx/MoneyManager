package com.hulkdx.moneymanagerv2.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hulkdx.moneymanagerv2.BuildConfig
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.components.inject
import com.hulkdx.moneymanagerv2.ui.transaction.ListTransactionsFragment
import com.hulkdx.moneymanagerv2.util.replaceFragment
import hulkdx.com.domain.data.remote.RemoteStatus
import kotlinx.android.synthetic.main.tutorial_fragment_login.*
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
class LoginFragment : Fragment() {

    @Inject
    lateinit var mLoginViewModel: LoginViewModel

    // region Lifecycle ---------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBtn.setOnClickListener {
            val userName = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginLoading()
            mLoginViewModel.login(userName, password)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLoginViewModel.getUserLoggedIn().observe(this, Observer {

            if (BuildConfig.DEBUG) {
                it.throwable?.message?.apply {
                    Toast.makeText(context, this,Toast.LENGTH_LONG).show()
                }
            }

            when (it.status) {
                RemoteStatus.SUCCESS -> {
                    loginSuccessful()
                }
                RemoteStatus.AUTH_ERROR -> {
                    loginFailedWrongCredential()
                }
                RemoteStatus.NETWORK_ERROR -> {
                    loginFailedNetworkError()
                }
                RemoteStatus.GENERAL_ERROR -> {
                    loginFailedGeneralError()
                }
            }
        })
    }

    // endregion Lifecycle -------------------------------------------------------------
    // region Login Callbacks -------------------------------------------------------------

    private fun loginLoading() {
        errorTextView.visibility = View.GONE
    }

    private fun loginSuccessful() {
        replaceFragment(R.id.container, ListTransactionsFragment())
    }

    private fun loginFailedGeneralError() {
        errorTextView.text = getString(R.string.generalError)
        errorTextView.visibility = View.VISIBLE
    }

    private fun loginFailedWrongCredential() {
        errorTextView.text = getString(R.string.authError)
        errorTextView.visibility = View.VISIBLE
    }

    private fun loginFailedNetworkError() {
        errorTextView.text = getString(R.string.networkError)
        errorTextView.visibility = View.VISIBLE
    }

    // endregion Login Callbacks -------------------------------------------------------------

}

