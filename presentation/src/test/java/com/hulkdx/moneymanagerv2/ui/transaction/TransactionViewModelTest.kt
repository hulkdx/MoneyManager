package com.hulkdx.moneymanagerv2.ui.transaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.hulkdx.moneymanagerv2.ui.anyKotlin
import com.nhaarman.mockitokotlin2.verify
import hulkdx.com.domain.usecase.TransactionCategoryUseCase
import hulkdx.com.domain.usecase.TransactionUseCase
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
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
    fun loadTransactions_callLoadingFirst() {
        // Arrange
        // Act
        SUT.loadTransactions()
        // Assert
        assertTrue(SUT.getTransactionResult().value is TransactionResult.Loading)
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