package com.hulkdx.moneymanagerv2.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hulkdx.moneymanagerv2.ui.anyKotlin
import com.hulkdx.moneymanagerv2.ui.capture
import hulkdx.com.domain.usecase.AuthUseCase
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.Mockito.*

@Suppress("PrivatePropertyName")
class RegisterViewModelTest {


    // region constants ----------------------------------------------------------------------------
    private val USERNAME  = "username"
    private val FIRST_NAME = "firstname"
    private val LAST_NAME  = "lastname"
    private val EMAIL     = "email"
    private val PASSWORD  = "password"
    private val CURRENCY  = "currency"

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @get:Rule var mMockitoJUnit = MockitoJUnit.rule()!!
    @get:Rule var mInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock lateinit var mAuthUseCase: AuthUseCase

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: RegisterViewModel

    @Before
    fun setup() {
        SUT = RegisterViewModel(mAuthUseCase)
    }

    @Test
    fun register_success_passToUseCase() {
        // Arrange
        val ac: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        // Act
        SUT.register(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, EMAIL, CURRENCY)
        // Assert
        verify(mAuthUseCase).registerAsync(capture(ac), capture(ac), capture(ac), capture(ac),
                capture(ac), capture(ac), anyKotlin())
    }

    // region helper methods -----------------------------------------------------------------------

    // endregion helper methods --------------------------------------------------------------------

}