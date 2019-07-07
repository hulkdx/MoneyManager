package com.hulkdx.moneymanagerv2.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hulkdx.moneymanagerv2.mapper.TransactionMapper
import com.hulkdx.moneymanagerv2.model.TransactionModel
import hulkdx.com.domain.usecase.TransactionCategoryUseCase
import hulkdx.com.domain.usecase.TransactionUseCase
import hulkdx.com.domain.usecase.TransactionUseCase.*
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class TransactionViewModel @Inject constructor(
        private val mTransactionMapper: TransactionMapper,
        private val mTransactionUseCase: TransactionUseCase,
        private val mTransactionCategoryUseCase: TransactionCategoryUseCase
) : ViewModel() {

    // region LiveData -----------------------------------------------------------------------------

    private var mGetTransactionsResult = MutableLiveData<GetTransactionViewModelResult>()
    private var mDeleteTransactionResult = MutableLiveData<DeleteTransactionViewModelResult>()
    private var mTransactionCategoryResult = MutableLiveData<TransactionCategoryViewModelResult>()

    fun getTransactionResult(): LiveData<GetTransactionViewModelResult> = mGetTransactionsResult

    fun deleteTransactionResult(): LiveData<DeleteTransactionViewModelResult>
            = mDeleteTransactionResult

    fun getTransactionCategoryResult(): LiveData<TransactionCategoryViewModelResult> =
            mTransactionCategoryResult

    // endregion LiveData --------------------------------------------------------------------------

    override fun onCleared() {
        super.onCleared()
        mTransactionUseCase.dispose()
        mTransactionCategoryUseCase.dispose()
    }

    // region Transactions -------------------------------------------------------------------------

    fun loadTransactions() {
        if (mGetTransactionsResult.value != null) {
            return
        }
        mGetTransactionsResult.value = GetTransactionViewModelResult.Loading
        mTransactionUseCase.getTransactionsAsync {
            val result: GetTransactionViewModelResult
            when (it) {
                is GetTransactionResult.AuthenticationError -> result = GetTransactionViewModelResult.AuthenticationError
                is GetTransactionResult.Success -> {

                    val transactionModels
                            = mTransactionMapper.mapTransactionList(it.transactions)

                    result = GetTransactionViewModelResult.Loaded(
                            transactionModels,
                            it.amount,
                            it.currencyName
                    )
                }
                is GetTransactionResult.NetworkError -> result = GetTransactionViewModelResult.NetworkError(it.throwable)
                is GetTransactionResult.GeneralError -> result = GetTransactionViewModelResult.GeneralError(it.throwable)
            }
            mGetTransactionsResult.value = result
        }
    }

    fun searchTransactions(searchText: String) {
        mTransactionUseCase.searchTransactionsAsync(searchText) {
            val transactionModels = mTransactionMapper.mapTransactionList(it)
            mGetTransactionsResult.value = GetTransactionViewModelResult.Loaded(transactionModels)
        }
    }

    fun deleteTransaction(positions: Set<Int>, id: List<Long>) {
        mDeleteTransactionResult.value = DeleteTransactionViewModelResult.Loading
        mTransactionUseCase.deleteTransactionsAsync(positions, id) {
            val result: DeleteTransactionViewModelResult
            when (it) {
                is DeleteTransactionResult.AuthenticationError -> {
                    val transactionModels = mTransactionMapper.mapTransactionList(it.oldTransactions)
                    result = DeleteTransactionViewModelResult.AuthenticationError(transactionModels)
                }
                is DeleteTransactionResult.Success -> {
                    val transactionModels = mTransactionMapper.mapTransactionList(it.newTransactions)
                    result = DeleteTransactionViewModelResult.Success(
                            it.amount,
                            transactionModels,
                            positions
                    )
                }
                is DeleteTransactionResult.NetworkError -> {
                    val transactionModels = mTransactionMapper.mapTransactionList(it.oldTransactions)
                    result = DeleteTransactionViewModelResult.NetworkError(transactionModels, it.throwable)
                }
                is DeleteTransactionResult.GeneralError -> {
                    val transactionModels = mTransactionMapper.mapTransactionList(it.oldTransactions)
                    result = DeleteTransactionViewModelResult.GeneralError(transactionModels, it.throwable)
                }
                is DeleteTransactionResult.UnknownError -> {
                    result = DeleteTransactionViewModelResult.UnknownError(it.throwable)
                }
            }
            mDeleteTransactionResult.value = result
        }
    }

    fun addTransaction(amount: String,
                       date: String,
                       categoryName: String?,
                       categoryHexColor: String?,
                       attachment: String?) {
        // TODO
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
    // region ViewModelResult ----------------------------------------------------------------------

    sealed class GetTransactionViewModelResult {
        object Loading: GetTransactionViewModelResult()
        class Loaded(
                val transactions: List<TransactionModel>,
                val transactionsTotalAmount: String? = null,
                val transactionsCurrencyName: String? = null
        ): GetTransactionViewModelResult()
        object AuthenticationError : GetTransactionViewModelResult()
        class NetworkError(val throwable: Throwable): GetTransactionViewModelResult()
        class GeneralError(val throwable: Throwable? = null): GetTransactionViewModelResult()
    }

    sealed class DeleteTransactionViewModelResult {
        object Loading: DeleteTransactionViewModelResult()

        class AuthenticationError(
                val oldTransactions: List<TransactionModel>): DeleteTransactionViewModelResult()
        class NetworkError(
                val oldTransactions: List<TransactionModel>,
                val throwable:       Throwable): DeleteTransactionViewModelResult()
        class GeneralError(
                val oldTransactions: List<TransactionModel>,
                val throwable:       Throwable? = null): DeleteTransactionViewModelResult()
        class UnknownError(val throwable: Throwable): DeleteTransactionViewModelResult()
        class Success(
                val transactionsTotalAmount: String,
                val newTransactions:         List<TransactionModel>,
                val deletedPositions: Set<Int>): DeleteTransactionViewModelResult()
    }

    sealed class TransactionCategoryViewModelResult {
        class Success(): TransactionCategoryViewModelResult()
        object Loading: TransactionCategoryViewModelResult()
        object AuthenticationError : TransactionCategoryViewModelResult()
        class NetworkError(val throwable: Throwable): TransactionCategoryViewModelResult()
        class GeneralError(val throwable: Throwable? = null): TransactionCategoryViewModelResult()
    }

    // endregion ViewModelResult -------------------------------------------------------------------

}