/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.ui.base.BaseActivity;

import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject MainPresenter mMainPresenter;
    @Inject PreferencesHelper mPrefrencesHelper;
    @Inject TransactionAdapter mTransactionAdapter;

    @BindView(R.id.tv_balance) TextView balanceTextView;
    @BindView(R.id.tv_empty_list) TextView emptyListTextView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setAdapter(mTransactionAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainPresenter.attachView(this);
        mMainPresenter.loadTransactions();

        balanceTextView.setText(String.valueOf(mPrefrencesHelper.getUserMoney()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showEmptyTransactions() {
        emptyListTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTransactions(List<Transaction> transactions) {
        emptyListTextView.setVisibility(View.GONE);
    }

}
