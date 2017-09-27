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
import android.util.SparseLongArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import timber.log.Timber;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {
    // This can be all transactions or filteredTransactions (searched transaction)
    private List<Transaction> mTransactions;
    // All transactions
    private List<Transaction> mAllTransactions;
    private Context mContext;
    private String mCurrencyName;

    private boolean mShowCheckBox = false;
    private boolean isCheckBoxHidden = false;
    private SparseLongArray mSelectedTransactions;

    private boolean mCheckAllCheckBox = false;
    private boolean mCheckNoneCheckBox = false;

    @Inject
    TransactionAdapter() {
        mTransactions = new ArrayList<>();
        mAllTransactions = new ArrayList<>();
        mSelectedTransactions = new SparseLongArray();
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

        if (mCheckAllCheckBox) {
            holder.checkBox.setChecked(true);
            if (position + 1 == mTransactions.size()) {
                mCheckAllCheckBox = false;
            }
            return;
        } else if (mCheckNoneCheckBox) {
            holder.checkBox.setChecked(false);
            if (position + 1 == mTransactions.size()) {
                mCheckNoneCheckBox = false;
            }
            return;
        }

        Transaction transaction  = mTransactions.get(position);

        // Only set item position for the first time.
        holder.setItemPosition(position);

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
            holder.balanceNumberTV.setTextColor(
                    ContextCompat.getColor(mContext, R.color.black));
            holder.balanceCurrencyTV.setTextColor(
                    ContextCompat.getColor(mContext, R.color.black));
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
        }

        if (mShowCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.getDateDayLayoutParams().removeRule(RelativeLayout.ALIGN_PARENT_START);
            holder.getDateDayLayoutParams().addRule(RelativeLayout.END_OF, R.id.checkBox);
        } else if (!isCheckBoxHidden) {
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.GONE);
            holder.getDateDayLayoutParams().addRule(RelativeLayout.ALIGN_PARENT_START);
            isCheckBoxHidden = true;
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

    /***
     * Send only the value of mSelectedTransactions to the MainActivity.
     * 
     * @return an array of (long) id selected.
     */
    long[] getSelectedItems() {
        int selectedItemsSize = mSelectedTransactions.size();
        long[] arraySelectedTransactionsId = new long[selectedItemsSize];
        for (int i = 0; i<selectedItemsSize; i++) {
            arraySelectedTransactionsId[i] = mSelectedTransactions.valueAt(i);
        }
        return arraySelectedTransactionsId;
    }

    void checkAllCheckBoxes() {
        mCheckAllCheckBox = true;
    }

    void checkNonCheckBoxes() {
        mCheckNoneCheckBox = true;
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
        private int itemPosition;

        TransactionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            dateDayLayoutParams = (RelativeLayout.LayoutParams) dateDayTV.getLayoutParams();
        }

        RelativeLayout.LayoutParams getDateDayLayoutParams() {
            return dateDayLayoutParams;
        }

        void setItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        @OnCheckedChanged(R.id.checkBox)
        void onCheckedChanged(CompoundButton compoundButton, boolean check) {
            if (check) {
                mSelectedTransactions.append(itemPosition, mTransactions.get(itemPosition).getId());
            } else {
                mSelectedTransactions.delete(itemPosition);
            }
        }

        // Open the attachment picture.
        @OnClick(R.id.attachment_view)
        void onClickAttachment() {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mTransactions.get(itemPosition).getAttachment()));
            // According to FileProvider docs this flag is required.
            // @link https://developer.android.com/reference/android/support/v4/content/FileProvider.html
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
            }
        }

    }
}
