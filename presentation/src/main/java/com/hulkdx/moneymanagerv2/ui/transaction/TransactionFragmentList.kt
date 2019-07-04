package com.hulkdx.moneymanagerv2.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hulkdx.moneymanagerv2.BuildConfig
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.inject
import com.hulkdx.moneymanagerv2.model.TransactionModel
import com.hulkdx.moneymanagerv2.ui.transaction.TransactionViewModel.TransactionViewModelResult.*
import com.hulkdx.moneymanagerv2.util.ViewModelFactory
import com.hulkdx.moneymanagerv2.util.getViewModel
import kotlinx.android.synthetic.main.transaction_fragment_list.*
import kotlinx.android.synthetic.main.transaction_main_list_view.*
import javax.inject.Inject

/**
 * TODO: change the layout to use ConstraintLayout.
 *
 * Created by Mohammad Jafarzadeh Rezvan on 28/06/2019.
 */
class TransactionFragmentList: Fragment(), SearchView.OnQueryTextListener {

    private lateinit var mTransactionViewModel: TransactionViewModel
    @Inject lateinit var mTransactionListAdapter: TransactionListAdapter
    @Inject lateinit var mViewModelFactory: ViewModelFactory

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

        mTransactionViewModel = getViewModel(mViewModelFactory)

        setupUI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mTransactionViewModel.loadTransactions()
        mTransactionViewModel.loadTransactionCategories()

        mTransactionViewModel.getTransactionResult().observe(this, Observer { result ->
            when (result) {
                is Loading             -> transactionsLoading()
                is AuthenticationError -> transactionsAuthError()
                is NetworkError        -> transactionsNetworkError(result.throwable)
                is GeneralError        -> transactionsGeneralError(result.throwable)
                is Success             -> {
                    transactionsSuccessful(result.transactions)
                    if (result.transactionsTotalAmount != null) {
                        transactionsTotalAmountSuccessful(result.transactionsTotalAmount)
                    }
                    if (result.transactionsCurrencyName != null) {
                        transactionsCurrencyNameSuccessful(result.transactionsCurrencyName)
                    }
                }
            }
        })

        mTransactionViewModel.getTransactionCategoryResult().observe(this, Observer { result ->
            when (result) {
                // TODO
            }
        })
    }

    // endregion Lifecycle -------------------------------------------------------------------------
    // region Transaction Callbacks ----------------------------------------------------------------

    private fun transactionsLoading() {

    }

    private fun transactionsSuccessful(transactions: List<TransactionModel>) {
        emptyTextView.visibility = if (transactions.isEmpty()) View.VISIBLE else View.GONE
        mTransactionListAdapter.mTransactions = transactions
        mTransactionListAdapter.notifyDataSetChanged()
    }

    private fun transactionsTotalAmountSuccessful(totalAmount: String) {
        balanceTextView.text = totalAmount
    }

    private fun transactionsCurrencyNameSuccessful(currencyName: String) {
        mTransactionListAdapter.mCurrencyName = currencyName
        mTransactionListAdapter.notifyDataSetChanged()
    }

    private fun transactionsNetworkError(throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            throwable?.message?.apply {
                Toast.makeText(context, this, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun transactionsGeneralError(throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            throwable?.message?.apply {
                Toast.makeText(context, this, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun transactionsAuthError() {
        // TODO logout...
    }

    // endregion Transaction Callbacks -------------------------------------------------------------
    // region UI setup -----------------------------------------------------------------------------

    private fun setupUI() {
        transactionRecyclerView.adapter = mTransactionListAdapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)

        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            mTransactionViewModel.searchTransactions(it)
        }
        return false
    }

    // endregion UI setup --------------------------------------------------------------------------
}
