package com.hulkdx.moneymanagerv2.ui.auth

import com.hulkdx.moneymanagerv2.ui.auth.util.anyCustom
import hulkdx.com.domain.interactor.AuthUseCase
import io.reactivex.functions.Consumer
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit


/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@Suppress("UNCHECKED_CAST")
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

        verify(mAuthUseCase).isLoggedInSync()
    }

    @Test
    fun `userLoggedInLiveData calls AuthUseCase`() {
        authViewModel.userLoggedInLiveData()

        verify(mAuthUseCase).isLoggedInAsync(anyCustom())
    }

    @Test
    fun `userLoggedInLiveData value changes with AuthUseCase response`() {

        val result = true
        val consumer: Consumer<Boolean> = mock(Consumer::class.java) as Consumer<Boolean>
        `when`(mAuthUseCase.isLoggedInAsync(anyCustom())).then { consumer.accept(result) }

        authViewModel.userLoggedInLiveData()

        verify(consumer).accept(result)
    }
}