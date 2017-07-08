/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/8/2017.
 */
package com.hulkdx.moneymanager.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hulkdx.moneymanager.R;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.ButterKnife;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private List<String> mTransactions;

    @Inject
    public TransactionAdapter() {
        mTransactions = new ArrayList<>();
        mTransactions.add("saba");
        mTransactions.add("saba");
    }

    public void setTransactions(List<String> transactions) {
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

    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.text_name) public TextView name;

        public TransactionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
