package com.hulkdx.moneymanagerv2.ui.register

import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers
import org.mockito.junit.MockitoJUnit
import org.mockito.Mockito.*

class RegisterViewModelTest {


    // region constants ----------------------------------------------------------------------------
    private val USERNAME = "username"
    private val PASSWORD = "password"

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @get:Rule var mMockitoJUnit = MockitoJUnit.rule()!!

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: RegisterViewModel

    @Before
    fun setup() {
        SUT = RegisterViewModel()
    }

    @Test
    fun register_success_passToUseCase() {
        // TODO Arrange
        // Act
//        SUT.register(USERNAME, PASSWORD)
        // Assert
    }

    // region helper methods -----------------------------------------------------------------------

    // endregion helper methods --------------------------------------------------------------------

}