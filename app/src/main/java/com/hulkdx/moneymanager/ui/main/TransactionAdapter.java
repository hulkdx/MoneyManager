/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/8/2017.
 */
package com.hulkdx.moneymanager.ui.main;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.injection.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private List<Transaction> mTransactions;
    private Context mContext;

    @Inject
    public TransactionAdapter(@ApplicationContext Context context) {
        mContext = context;
        mTransactions = new ArrayList<>();
    }

    public void setTransactions(List<Transaction> transactions) {
        mTransactions = transactions;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_transaction, parent, false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        // Set background of the layout on odd position to white
        if (position % 2 == 1) {
            holder.rootLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }
        if (mTransactions.get(position).isAmountPositive()) {
            holder.balanceNumberTV.setText(mContext.getString(R.string.balance_value_positive,
                    mTransactions.get(position).getAmount()));
            holder.balanceNumberTV.setTextColor(ContextCompat.getColor(mContext, R.color.darkgreen));
            holder.balanceSignTV.setTextColor(ContextCompat.getColor(mContext, R.color.darkgreen));
        } else {
            holder.balanceNumberTV.setText(mContext.getString(R.string.balance_value_negative,
                     mTransactions.get(position).getAmount() * -1));
        }
        holder.dateMonthTV.setText(mTransactions.get(position).getMonth());
        holder.dateDayTV.setText(mTransactions.get(position).getDay());
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout) LinearLayout rootLayout;
        @BindView(R.id.balance_number) TextView balanceNumberTV;
        @BindView(R.id.balance_sign) TextView balanceSignTV;
        @BindView(R.id.date_month) TextView dateMonthTV;
        @BindView(R.id.date_day) TextView dateDayTV;

        public TransactionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
