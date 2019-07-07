package com.hulkdx.moneymanagerv2.ui.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hulkdx.moneymanagerv2.BuildConfig
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.inject
import com.hulkdx.moneymanagerv2.model.TransactionModel
import com.hulkdx.moneymanagerv2.ui.transaction.TransactionViewModel.*
import com.hulkdx.moneymanagerv2.ui.transaction.TransactionViewModel.GetTransactionViewModelResult.*
import com.hulkdx.moneymanagerv2.util.ViewModelFactory
import com.hulkdx.moneymanagerv2.util.getViewModel
import kotlinx.android.synthetic.main.transaction_fragment_list.*
import kotlinx.android.synthetic.main.transaction_main_list_view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * TODO: change the layout to use ConstraintLayout.
 *
 * Created by Mohammad Jafarzadeh Rezvan on 28/06/2019.
 */
class TransactionFragmentList: Fragment() {

    private val mTAG = "TransactionFragmentList"
    private lateinit var mTransactionViewModel: TransactionViewModel
    @Inject lateinit var mTransactionListAdapter: TransactionListAdapter
    @Inject lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mMiddleRelativeLayoutParam: RelativeLayout.LayoutParams
    private lateinit var mDatePicker: DatePicker

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
                is AuthenticationError -> authErrorLogout()
                is NetworkError        -> transactionsNetworkError(result.throwable)
                is GeneralError        -> transactionsGeneralError(result.throwable)
                is Loaded              -> {
                    transactionsLoaded(result.transactions)
                    if (result.transactionsTotalAmount != null) {
                        transactionsTotalAmountSuccessful(result.transactionsTotalAmount)
                    }
                    if (result.transactionsCurrencyName != null) {
                        transactionsCurrencyNameSuccessful(result.transactionsCurrencyName)
                    }
                }
            }
        })

        mTransactionViewModel.deleteTransactionResult().observe(this, Observer { result ->
            when (result) {
                DeleteTransactionViewModelResult.Loading -> transactionsLoading()
                is DeleteTransactionViewModelResult.AuthenticationError -> {
                    transactionsDeleteRevertBack(result.oldTransactions)
                    authErrorLogout()
                }
                is DeleteTransactionViewModelResult.NetworkError ->
                    transactionsDeleteRevertBack(result.oldTransactions, result.throwable)
                is DeleteTransactionViewModelResult.GeneralError ->
                    transactionsDeleteRevertBack(result.oldTransactions, result.throwable)
                is DeleteTransactionViewModelResult.Success -> {
                    transactionsDeleted(result.newTransactions, result.deletedPositions)
                    transactionsTotalAmountSuccessful(result.transactionsTotalAmount)
                }
                is DeleteTransactionViewModelResult.UnknownError -> {
                    showFixTheBug(result.throwable)
                }
            }
        })

        mTransactionViewModel.getTransactionCategoryResult().observe(this, Observer { result ->
            //when (result) {
                // TODO
            // }
        })
    }

    // endregion Lifecycle -------------------------------------------------------------------------
    // region UI setup -----------------------------------------------------------------------------

    private fun setupUI() {

        mMiddleRelativeLayoutParam = middleRelativeLayout.layoutParams as RelativeLayout.LayoutParams
        mDatePicker = date_picker as DatePicker

        setupTransactionRecyclerView()

        setupSearchView()

        setupDeleteImageView()

        setupBottomLayout()
    }

    private fun setupTransactionRecyclerView() {
        transactionRecyclerView.hasFixedSize()
        transactionRecyclerView.adapter = mTransactionListAdapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        transactionRecyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && bottomLayoutExpanded.visibility == View.VISIBLE) {
                hideBottomLayout()
            }
            return@setOnTouchListener false
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    mTransactionViewModel.searchTransactions(it)
                }
                return false
            }
        })
    }

    private fun setupDeleteImageView() {
        var isDeleteSelected = false
        deleteImageView.setOnClickListener {
            isDeleteSelected = !isDeleteSelected

            if (isDeleteSelected) {
                deleteBtnSelected()
            } else {
                deleteBtnUnSelected()
            }
        }
    }

    private fun setupBottomLayout() {
        bottomLayout.setOnClickListener {
            expandBottomLayout()
        }
        addNewTransactionsEditText.setOnEditorActionListener { tv, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTransaction()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener true
        }
    }

    private fun expandBottomLayout() {

        addNewTransactionsEditText.setText("")
        // TODO:
        // mCategoryAdapter.resetSelectedCategories()
        // mSelectedCategoryId = -1
        // mSelectedAttachment = null

        bottomLayoutExpanded.visibility = View.VISIBLE
        bottomLayout.visibility = View.GONE
        mMiddleRelativeLayoutParam.addRule(RelativeLayout.ABOVE, R.id.bottomLayoutExpanded)
        // TODO show keyboard
    }

    private fun hideBottomLayout() {
        bottomLayoutExpanded.visibility = View.GONE
        bottomLayout.visibility = View.VISIBLE
        mMiddleRelativeLayoutParam.addRule(RelativeLayout.ABOVE, R.id.bottomLayout)
    }

    // endregion UI setup --------------------------------------------------------------------------

    // region Transaction --------------------------------------------------------------------------

    // region Load Transaction Callbacks -----------------------------------------------------------

    private fun transactionsLoading() {

    }

    private fun transactionsLoaded(transactions: List<TransactionModel>) {
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
        showThrowableOnDebug(throwable)
    }

    private fun transactionsGeneralError(throwable: Throwable?) {
        showThrowableOnDebug(throwable)
    }

    // endregion Load Transaction Callbacks --------------------------------------------------------
    // region Delete Transaction -------------------------------------------------------------------

    private fun deleteBtnSelected() {
        // Show the 'select all checkbox'
        // TODO selectAllCheckBox.visibility = View.VISIBLE

        deleteImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red),
                android.graphics.PorterDuff.Mode.MULTIPLY)

        // Show checkboxes in the list view.
        // Note: should this logic be done in TransactionViewModel?
        mTransactionListAdapter.checkbox(true)
    }

    private fun deleteBtnUnSelected() {
        // selectAllCheckBox.visibility = View.GONE

        deleteImageView.colorFilter = null

        mTransactionListAdapter.checkbox(false)
        val id: List<Long> = ArrayList(mTransactionListAdapter.mCheckedItemIds)
        val positions: Set<Int> = TreeSet(mTransactionListAdapter.mCheckedItemPositions)
        Log.d(mTAG, positions.toString())
        if (id.isNotEmpty()) {
            mTransactionViewModel.deleteTransaction(
                    positions,
                    id
            )
        }
        mTransactionListAdapter.checkboxClear()
    }

    // endregion Delete Transaction ----------------------------------------------------------------
    // region Delete Transaction Callbacks ---------------------------------------------------------

    private fun transactionsDeleted(transactions: List<TransactionModel>, deletedPositions: Set<Int>) {
        Log.d(mTAG, "transactionsDeleted $transactions positions: $deletedPositions")
        mTransactionListAdapter.mTransactions = transactions
        for (position in deletedPositions.reversed()) {
            mTransactionListAdapter.notifyItemRemoved(position)
        }
        emptyTextView.visibility = if (transactions.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun transactionsDeleteRevertBack(oldTransactions: List<TransactionModel>,
                                             throwable: Throwable? = null) {
        Toast.makeText(context, R.string.error_deleting_transactions, Toast.LENGTH_SHORT).show()
        showThrowableOnDebug(throwable)
        transactionsLoaded(oldTransactions)
    }

    // endregion Delete Transaction Callbacks ------------------------------------------------------
    // region Add Transaction ----------------------------------------------------------------------

    private fun addTransaction() {
        val amount = addNewTransactionsEditText.text.toString()
        val date = "${mDatePicker.year}-${mDatePicker.month + 1}-${mDatePicker.dayOfMonth}"
        // TODO add category
        // TODO add attachment
         mTransactionViewModel.addTransaction(amount, date, null, null, null)
    }

    // endregion Add Transaction -------------------------------------------------------------------

    // endregion Transaction -----------------------------------------------------------------------

    // region Extra --------------------------------------------------------------------------------

    private fun showThrowableOnDebug(throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            throwable?.message?.apply {
                Toast.makeText(context, this, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showFixTheBug(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            val text = "Please fix the bug :: ${throwable.message}"
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }

    private fun authErrorLogout() {
        // TODO authErrorLogout...
    }

    // endregion Extra -----------------------------------------------------------------------------

}
