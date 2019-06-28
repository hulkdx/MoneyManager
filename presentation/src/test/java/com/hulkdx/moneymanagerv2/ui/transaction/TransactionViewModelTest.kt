package com.hulkdx.moneymanagerv2.ui.transaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hulkdx.moneymanagerv2.ui.anyKotlin
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import hulkdx.com.domain.usecase.TransactionCategoryUseCase
import hulkdx.com.domain.usecase.TransactionUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

@Suppress("PrivatePropertyName")
class TransactionViewModelTest {

    // region constants ----------------------------------------------------------------------------

    // endregion constants -------------------------------------------------------------------------
    // region helper fields ------------------------------------------------------------------------

    @get:Rule var mMockitoJUnit = MockitoJUnit.rule()!!
    @get:Rule var mInstantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock lateinit var mTransactionUseCase: TransactionUseCase
    @Mock lateinit var mTransactionCategoryUseCase: TransactionCategoryUseCase

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: TransactionViewModel

    @Before
    fun setup() {
        SUT = TransactionViewModel(mTransactionUseCase, mTransactionCategoryUseCase)
    }

    @Test
    fun loadTransactions_passToUseCase() {
        // Arrange
        // Act
        SUT.loadTransactions()
        // Assert
        verify(mTransactionUseCase).getTransactions(anyKotlin())
    }

    @Test
    fun loadTransactionCategories_passToUseCase() {
        // Arrange
        // Act
        SUT.loadTransactionCategories()
        // Assert
        verify(mTransactionCategoryUseCase).getTransactionCategories(anyKotlin())
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------
}