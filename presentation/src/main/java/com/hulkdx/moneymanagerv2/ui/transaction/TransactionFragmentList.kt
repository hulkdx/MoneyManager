package com.hulkdx.moneymanagerv2.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.inject
import com.hulkdx.moneymanagerv2.util.getViewModel
import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.usecase.TransactionUseCase
import kotlinx.android.synthetic.main.transaction_fragment_list.*

/**
 * TODO: change the layout to use ConstraintLayout.
 *
 * Created by Mohammad Jafarzadeh Rezvan on 28/06/2019.
 */
class TransactionFragmentList: Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel

    // region Lifecycle ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.transaction_fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTransactionViewModel = getViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mTransactionViewModel.loadTransactions()
        mTransactionViewModel.loadTransactionCategories()

        mTransactionViewModel.getTransactionResult().observe(this, Observer { result ->
            when (result) {
                TransactionUseCase.TransactionResult.Loading -> {
                    transactionsLoading()
                }
                TransactionUseCase.TransactionResult.AuthenticationError -> transactionsAuthError()
                is TransactionUseCase.TransactionResult.Success -> transactionsSuccessful(
                        result.amount, result.transactions)
                is TransactionUseCase.TransactionResult.NetworkError -> transactionsNetworkError(
                        result.throwable)
                is TransactionUseCase.TransactionResult.GeneralError -> transactionsGeneralError(
                        result.throwable)
            }
        })
    }

    // endregion Lifecycle -------------------------------------------------------------------------
    // region Transaction Callbacks ----------------------------------------------------------------

    private fun transactionsLoading() {

    }

    private fun transactionsSuccessful(totalAmount: String, transactions: List<Transaction>) {
        emptyTextView.visibility = if (transactions.isEmpty()) View.VISIBLE else View.GONE
        balanceTextView.text = totalAmount
    }

    private fun transactionsNetworkError(throwable: Throwable?) {

    }

    private fun transactionsGeneralError(throwable: Throwable?) {

    }

    private fun transactionsAuthError() {
        // TODO logout...
    }

    // endregion Transaction Callbacks -------------------------------------------------------------
}
