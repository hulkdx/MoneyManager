package com.hulkdx.moneymanagerv2.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hulkdx.moneymanagerv2.mapper.TransactionMapper
import com.hulkdx.moneymanagerv2.model.TransactionModel
import hulkdx.com.domain.usecase.TransactionCategoryUseCase
import hulkdx.com.domain.usecase.TransactionUseCase
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class TransactionViewModel @Inject constructor(
        private val mTransactionMapper: TransactionMapper,
        private val mTransactionUseCase: TransactionUseCase,
        private val mTransactionCategoryUseCase: TransactionCategoryUseCase
) : ViewModel() {

    private var mTransactionResult = MutableLiveData<TransactionViewModelResult>()
    private var mTransactionCategoryResult = MutableLiveData<TransactionCategoryViewModelResult>()

    fun getTransactionResult(): LiveData<TransactionViewModelResult> = mTransactionResult

    fun getTransactionCategoryResult(): LiveData<TransactionCategoryViewModelResult> =
            mTransactionCategoryResult

    override fun onCleared() {
        super.onCleared()
        mTransactionUseCase.dispose()
        mTransactionCategoryUseCase.dispose()
    }

    // region Transactions -------------------------------------------------------------------------

    fun loadTransactions() {
        if (mTransactionResult.value != null) {
            return
        }
        mTransactionResult.value = TransactionViewModelResult.Loading
        mTransactionUseCase.getTransactionsAsync {
            val result: TransactionViewModelResult
            when (it) {
                is TransactionResult.AuthenticationError -> result = TransactionViewModelResult.AuthenticationError
                is TransactionResult.Success -> {
                    val transactionModels = mTransactionMapper.mapTransactionList(it.transactions)
                    result = TransactionViewModelResult.Success(
                            transactionModels,
                            it.amount,
                            it.currencyName
                    )
                }
                is TransactionResult.NetworkError -> result = TransactionViewModelResult.NetworkError(it.throwable)
                is TransactionResult.GeneralError -> result = TransactionViewModelResult.GeneralError(it.throwable)
            }
            mTransactionResult.value = result
        }
    }

    fun searchTransactions(searchText: String) {
        mTransactionUseCase.searchTransactionsAsync(searchText) {
            val transactionModels = mTransactionMapper.mapTransactionList(it)
            mTransactionResult.value = TransactionViewModelResult.Success(transactionModels)
        }
    }

    fun deleteTransaction() {

    }

    // endregion Transactions ----------------------------------------------------------------------
    // region Category -----------------------------------------------------------------------------

    fun loadTransactionCategories() {
        if (mTransactionCategoryResult.value != null) {
            return
        }
        mTransactionCategoryResult.value = TransactionCategoryViewModelResult.Loading
        mTransactionCategoryUseCase.getTransactionCategories {
            // TODO
            // mTransactionCategoryResult.value = it
        }
    }

    // endregion Category --------------------------------------------------------------------------

    sealed class TransactionViewModelResult {
        object Loading: TransactionViewModelResult()
        object AuthenticationError : TransactionViewModelResult()
        class Success(val transactions: List<TransactionModel>,
                      val transactionsTotalAmount: String? = null,
                      val transactionsCurrencyName: String? = null) : TransactionViewModelResult()
        class NetworkError(val throwable: Throwable): TransactionViewModelResult()
        class GeneralError(val throwable: Throwable? = null): TransactionViewModelResult()
    }

    sealed class TransactionCategoryViewModelResult {
        class Success(): TransactionCategoryViewModelResult()
        object Loading: TransactionCategoryViewModelResult()
        object AuthenticationError : TransactionCategoryViewModelResult()
        class NetworkError(val throwable: Throwable): TransactionCategoryViewModelResult()
        class GeneralError(val throwable: Throwable? = null): TransactionCategoryViewModelResult()
    }
}