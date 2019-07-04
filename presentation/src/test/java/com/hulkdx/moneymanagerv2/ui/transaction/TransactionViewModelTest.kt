package com.hulkdx.moneymanagerv2.ui.transaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hulkdx.moneymanagerv2.mapper.TransactionMapper
import com.hulkdx.moneymanagerv2.ui.anyKotlin
import com.hulkdx.moneymanagerv2.ui.transaction.TransactionViewModel.TransactionViewModelResult.Loading
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.verify
import hulkdx.com.domain.data.model.Category
import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.usecase.TransactionCategoryUseCase
import hulkdx.com.domain.usecase.TransactionUseCase
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

@Suppress("PrivatePropertyName", "MemberVisibilityCanBePrivate", "PropertyName")
class TransactionViewModelTest {

    // region constants ----------------------------------------------------------------------------

    val TEST_TRANSACTION_TOTAL_AMOUNT = "5512"
    val TEST_TRANSACTION_CURRENCY = "EURO"
    val TEST_TRANSACTION_1_ID = 0L
    val TEST_TRANSACTION_1_DATE = "2018-09-29"
    val TEST_TRANSACTION_1_CATEGORY = Category(9, "Shopping", "#FF00FF")
    val TEST_TRANSACTION_1_AMOUNT = -20F
    val TEST_TRANSACTION_1_ATTACHMENT = "/storage/emulated/0/Pictures/MoneyManager_20181029_131212.jpg"

    val TEST_TRANSACTION_LIST = listOf(
            Transaction(TEST_TRANSACTION_1_ID, TEST_TRANSACTION_1_DATE, TEST_TRANSACTION_1_CATEGORY,
                    TEST_TRANSACTION_1_AMOUNT, TEST_TRANSACTION_1_ATTACHMENT)
    )

    // endregion constants -------------------------------------------------------------------------
    // region helper fields ------------------------------------------------------------------------

    @get:Rule var mMockitoJUnit = MockitoJUnit.rule()!!
    @get:Rule var mInstantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock lateinit var mTransactionUseCase: TransactionUseCase
    @Mock lateinit var mTransactionCategoryUseCase: TransactionCategoryUseCase
    @Mock lateinit var mTransactionMapper: TransactionMapper

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: TransactionViewModel

    @Before
    fun setup() {
        SUT = TransactionViewModel(mTransactionMapper, mTransactionUseCase,
                mTransactionCategoryUseCase)
    }

    @Test
    fun loadTransactions_passToUseCase() {
        // Arrange
        // Act
        SUT.loadTransactions()
        // Assert
        verify(mTransactionUseCase).getTransactionsAsync(anyKotlin())
    }

    @Test
    fun loadTransactions_callLoadingFirst() {
        // Arrange
        // Act
        SUT.loadTransactions()
        // Assert
        assertTrue(SUT.getTransactionResult().value is Loading)
    }

    @Test
    fun loadTransactions_success_mapTransactionToTransactionModel() {
        // Arrange
        success()
        // Act
        SUT.loadTransactions()
        // Assert
        verify(mTransactionMapper).mapTransactionList(TEST_TRANSACTION_LIST, TEST_TRANSACTION_CURRENCY)
    }

    @Test
    fun searchTransactions_passToUseCase() {
        // Arrange
        val searchText = "SEARCH_TEXT"
        // Act
        SUT.searchTransactions(searchText)
        // Assert
        verify(mTransactionUseCase).searchTransactionsAsync(searchText, anyKotlin())
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

    private fun success() {
        doAnswer { invocation ->
            val argument = invocation.getArgument<Function1<TransactionResult, Unit>>(0)
            argument.invoke(TransactionResult.Success(TEST_TRANSACTION_LIST,
                    TEST_TRANSACTION_TOTAL_AMOUNT, TEST_TRANSACTION_CURRENCY))
            null
        }.`when`(mTransactionUseCase).getTransactionsAsync(anyKotlin())
    }

    // endregion helper methods --------------------------------------------------------------------
}