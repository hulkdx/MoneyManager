/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.ui.base.BaseActivity;

import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, View.OnClickListener {

    @Inject MainPresenter mMainPresenter;
    @Inject PreferencesHelper mPrefrencesHelper;
    @Inject TransactionAdapter mTransactionAdapter;

    @BindView(R.id.tv_balance) TextView mBalanceTextView;
    @BindView(R.id.tv_empty_list) TextView mEmptyListTextView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.bottom_layout) LinearLayout mBottomLayout;
    @BindView(R.id.et_add_new) EditText mAddNewEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBottomLayout.setOnClickListener(this);
        mAddNewEditText.setOnClickListener(this);

        mRecyclerView.setAdapter(mTransactionAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainPresenter.attachView(this);
        mMainPresenter.loadTransactions();

        mBalanceTextView.setText(getString(R.string.balance_value_euro, String.valueOf(mPrefrencesHelper.getUserMoney())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_layout:
            case R.id.et_add_new:
//                TODO what happens when clicking on this?
                break;

            default:
                break;
        }
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showEmptyTransactions() {
        mEmptyListTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTransactions(List<Transaction> transactions) {
        mEmptyListTextView.setVisibility(View.GONE);
    }

}
