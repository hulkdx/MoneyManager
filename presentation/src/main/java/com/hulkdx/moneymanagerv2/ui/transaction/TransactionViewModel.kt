package com.hulkdx.moneymanagerv2.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hulkdx.com.domain.usecase.TransactionCategoryUseCase
import hulkdx.com.domain.usecase.TransactionCategoryUseCase.*
import hulkdx.com.domain.usecase.TransactionUseCase
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class TransactionViewModel @Inject constructor(
        private val mTransactionUseCase: TransactionUseCase,
        private val mTransactionCategoryUseCase: TransactionCategoryUseCase
) : ViewModel() {

    private var mTransactionResult = MutableLiveData<TransactionResult>()
    private var mTransactionCategoryResult = MutableLiveData<TransactionCategoryResult>()

    fun getTransactionResult(): LiveData<TransactionResult> = mTransactionResult

    fun getTransactionCategoryResult(): LiveData<TransactionCategoryResult> = mTransactionCategoryResult

    override fun onCleared() {
        super.onCleared()
        mTransactionUseCase.dispose()
        mTransactionCategoryUseCase.dispose()
    }

    fun loadTransactions() {
        mTransactionUseCase.getTransactions{
            mTransactionResult.value = it
        }
    }

    fun loadTransactionCategories() {
        mTransactionCategoryUseCase.getTransactionCategories {
            mTransactionCategoryResult.value = it
        }
    }
}