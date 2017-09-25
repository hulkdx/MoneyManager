/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/8/2017.
 */
package com.hulkdx.moneymanagerv2.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hulkdx.moneymanagerv2.R;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {
    // This can be all transactions or filteredTransactions (searched transaction)
    private List<Transaction> mTransactions;
    // All transactions
    private List<Transaction> mAllTransactions;
    private Context mContext;
    private String mCurrencyName;

    private boolean mShowCheckBox = false;

    @Inject
    TransactionAdapter() {
        mTransactions = new ArrayList<>();
        mAllTransactions = new ArrayList<>();
    }

    public void setTransactions(List<Transaction> transactions) {
        mTransactions = transactions;
        mAllTransactions = transactions;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_transaction, parent, false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {

        Transaction transaction  = mTransactions.get(position);

        // Set background of the layout on odd position to white and grey on even.
        holder.rootLayout.setBackgroundColor(ContextCompat
                .getColor(mContext, (position % 2 == 0) ? R.color.white : R.color.grey));
        // Set balance text and colors
        if (mTransactions.get(position).isAmountPositive()) {
            holder.balanceNumberTV.setText(mContext.getString(R.string.balance_value_positive,
                    mTransactions.get(position).getAmount()));
            holder.balanceNumberTV.setTextColor(
                    ContextCompat.getColor(mContext, R.color.darkgreen));
            holder.balanceCurrencyTV.setTextColor(
                    ContextCompat.getColor(mContext, R.color.darkgreen));
        } else {
            holder.balanceNumberTV.setText(mContext.getString(R.string.balance_value_negative,
                     mTransactions.get(position).getAmount() * -1));
        }
        // Set Currency
        holder.balanceCurrencyTV.setText(mCurrencyName);

        // Date format equals to year-month-day
        String date = transaction.getDate();
        // Show Year?! String year = date.split("-")[0];
        String month = new DateFormatSymbols()
                        .getShortMonths()[ Integer.parseInt(date.split("-")[1]) - 1 ];
        String day = date.split("-")[2];
        holder.dateMonthTV.setText(month);
        holder.dateDayTV.setText(day);
        // Category
        if (transaction.getCategory() != null ) {
            holder.categoryNameTV.setText(transaction.getCategory().getName());

            holder.hexColorIV.setBackgroundColor(
                    Color.parseColor(transaction.getCategory().getHexColor()));
        }
        // Attachment
        if (transaction.getAttachment() != null &&
                !transaction.getAttachment().equals("") ) {

            holder.attachmentView.setVisibility(View.VISIBLE);
            // It will open the picture taken.
            // TODO Replace the click listener with the detail view of the item.
            holder.attachmentView.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(transaction.getAttachment()));
                // According to FileProvider docs this flag is required.
                // @link https://developer.android.com/reference/android/support/v4/content/FileProvider.html
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
            });

        }

        if (mShowCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.getDateDayLayoutParams().removeRule(RelativeLayout.ALIGN_PARENT_START);
            holder.getDateDayLayoutParams().addRule(RelativeLayout.END_OF, R.id.checkBox);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.getDateDayLayoutParams().addRule(RelativeLayout.ALIGN_PARENT_START);
        }
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    void setCurrencyName(String currencyName) {
        mCurrencyName = currencyName;
    }

    void filter(String text) {
        List<Transaction> filterTransaction = new ArrayList<>();
        if (text.isEmpty()) {
            mTransactions = mAllTransactions;
        } else {
            text = text.toLowerCase();
            boolean isNumber = text.matches("^-?\\d+.?(\\d+)?$");
            for (Transaction transaction: mAllTransactions) {
                // if text is numeric, search the balance
                if (isNumber) {
                    Timber.i(String.valueOf(transaction.getAmount()));
                    if (String.valueOf(transaction.getAmount()).toLowerCase().contains(text)) {
                        filterTransaction.add(transaction);
                    }
                    continue;
                }
                // else search category.
                if (transaction.getCategory() == null) {
                    continue;
                }

                if (transaction.getCategory().getName().toLowerCase().contains(text)) {
                    filterTransaction.add(transaction);
                }
            }
            mTransactions = filterTransaction;
        }
        notifyDataSetChanged();
    }

    void showCheckbox(boolean show) {
        mShowCheckBox = show;
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout) RelativeLayout rootLayout;
        @BindView(R.id.balance_number) TextView balanceNumberTV;
        @BindView(R.id.balance_sign) TextView balanceCurrencyTV;
        @BindView(R.id.date_month) TextView dateMonthTV;
        @BindView(R.id.date_day) TextView dateDayTV;
        @BindView(R.id.category_name_textview) TextView categoryNameTV;
        @BindView(R.id.view_hex_color) ImageView hexColorIV;
        @BindView(R.id.attachment_view) View attachmentView;
        @BindView(R.id.checkBox) CheckBox checkBox;

        // For performances: set LayoutParams on the constructor.
        private RelativeLayout.LayoutParams dateDayLayoutParams;

        TransactionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            dateDayLayoutParams = (RelativeLayout.LayoutParams) dateDayTV.getLayoutParams();
        }

        RelativeLayout.LayoutParams getDateDayLayoutParams() {
            return dateDayLayoutParams;
        }
    }
}
