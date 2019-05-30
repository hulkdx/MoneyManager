package com.hulkdx.moneymanagerv2.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.components.LoginComponent.Companion.inject
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
            mLoginViewModel.login(userName, password)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLoginViewModel.getUserLoggedIn().observe(this, Observer {
            when (it.status) {
                RemoteStatus.SUCCESS -> {
                    loginSuccessful()
                }
                RemoteStatus.AUTH_ERROR -> {
                    loginFailedWrongCredential()
                }
                RemoteStatus.NETWORK_ERROR, RemoteStatus.GENERAL_ERROR -> {
                    loginFailedGeneralError()
                }
            }
        })
    }

    // endregion Lifecycle -------------------------------------------------------------
    // region Login Callbacks -------------------------------------------------------------

    private fun loginSuccessful() {
        replaceFragment(R.id.container, ListTransactionsFragment())
    }

    private fun loginFailedGeneralError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loginFailedWrongCredential() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // endregion Login Callbacks -------------------------------------------------------------

}

