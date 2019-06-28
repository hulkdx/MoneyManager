package com.hulkdx.moneymanagerv2.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.inject
import com.hulkdx.moneymanagerv2.ui.register.RegisterViewModel
import com.hulkdx.moneymanagerv2.util.getViewModel

/**
 * TODO: change the layout to use ConstraintLayout.
 *
 * Created by Mohammad Jafarzadeh Rezvan on 28/06/2019.
 */
class TransactionFragmentList: Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel

    // region Lifecycle ---------------------------------------------------------------

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
    }

    // endregion Lifecycle -------------------------------------------------------------

}
