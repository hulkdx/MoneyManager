package com.hulkdx.moneymanagerv2.ui.auth

import hulkdx.com.domain.interactor.AuthUseCase
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit


/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@RunWith(JUnit4::class)
class AuthViewModelTest {

    @Rule @JvmField
    var mockitoRule = MockitoJUnit.rule()!!


    lateinit var mAuthUseCase: AuthUseCase
    lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        mAuthUseCase = Mockito.mock(AuthUseCase::class.java)
        authViewModel = AuthViewModel(mAuthUseCase)
    }

    @Test
    fun isUserLoggedInSync() {
        authViewModel.isUserLoggedInSync()

        verify(mAuthUseCase).isLoggedIn()
    }
}