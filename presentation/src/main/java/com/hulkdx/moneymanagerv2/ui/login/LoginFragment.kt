package com.hulkdx.moneymanagerv2.ui.login

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.hulkdx.moneymanagerv2.BuildConfig
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.inject
import com.hulkdx.moneymanagerv2.ui.register.RegisterFragment
import com.hulkdx.moneymanagerv2.ui.transaction.ListTransactionsFragment
import com.hulkdx.moneymanagerv2.util.getViewModel
import com.hulkdx.moneymanagerv2.util.replaceFragment
import hulkdx.com.domain.data.remote.RemoteStatus
import kotlinx.android.synthetic.main.tutorial_fragment_login.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
class LoginFragment : Fragment() {

    private lateinit var mLoginViewModel: LoginViewModel

    // region Lifecycle ---------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoginViewModel = getViewModel()
        setupLoginBtn()
        setupRegisterBtn()
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
    // region Setup Views -------------------------------------------------------------

    private fun setupLoginBtn() {
        loginBtn.setOnClickListener {
            val userName = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginLoading()
            mLoginViewModel.login(userName, password)
        }
    }

    private fun setupRegisterBtn() {
        val registerBtnAnimator = createRegisterBtnAnimator {
            replaceFragment(R.id.container, RegisterFragment())
        }
        registerBtn.setOnClickListener {
            registerBtnAnimator.start()
        }
    }

    // endregion Setup Views -------------------------------------------------------------
    // region Login Callbacks -------------------------------------------------------------

    private fun loginLoading() {
        loadingProgressBar.visibility = View.VISIBLE
        animateErrorTextView(false)
    }

    private fun loginSuccessful() {
        loadingProgressBar.visibility = View.GONE
        replaceFragment(R.id.container, ListTransactionsFragment())
    }

    private fun loginFailedGeneralError() {
        loadingProgressBar.visibility = View.GONE
        animateErrorTextView(true)
        errorTextView.text = getString(R.string.generalError)
    }

    private fun loginFailedWrongCredential() {
        loadingProgressBar.visibility = View.GONE
        animateErrorTextView(true)
        errorTextView.text = getString(R.string.authError)
    }

    private fun loginFailedNetworkError() {
        loadingProgressBar.visibility = View.GONE
        animateErrorTextView(true)
        errorTextView.text = getString(R.string.networkError)
    }

    // endregion Login Callbacks -------------------------------------------------------------
    // region Animations -------------------------------------------------------------

    private fun animateErrorTextView(isVisible: Boolean) {
        val transition = ChangeBounds()
        transition.addTarget(errorTextView)
        transition.duration = 150
        TransitionManager.beginDelayedTransition(loginContainer)
        errorTextView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private inline fun createRegisterBtnAnimator(crossinline onAnimatorEnd: () -> (Unit)): Animator {
        val animator = ObjectAnimator.ofFloat(registerBtn, "alpha",  0.4f, 1f)
        animator.duration = 120
        animator.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                onAnimatorEnd()
            }
        })
        return animator
    }

    // endregion Animations -------------------------------------------------------------
}

