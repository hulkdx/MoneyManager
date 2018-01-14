/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/8/2017.
 */
package com.hulkdx.moneymanagerv2.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.hulkdx.moneymanagerv2.injection.ActivityContext;
import com.hulkdx.moneymanagerv2.util.DialogFactory;
import com.hulkdx.moneymanagerv2.util.PermissionChecker;
import java.io.File;
import java.lang.ref.WeakReference;
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

    @Inject MainPresenter mMainPresenter;

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
    TransactionAdapter(@ActivityContext Context context) {
        mTransactions = new ArrayList<>();
        mAllTransactions = new ArrayList<>();
        mSelectedTransactions = new SparseLongArray();
        mContext = context;
    }

    public void setTransactions(List<Transaction> transactions) {
        mTransactions = transactions;
        mAllTransactions = transactions;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_transaction, parent, false);
        return new TransactionHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
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
        String[] splitDate = date.split("-");
        if (splitDate.length == 3) {
            String month = new DateFormatSymbols()
                    .getShortMonths()[ Integer.parseInt(splitDate[1]) - 1 ];
            String day = splitDate[2];
            holder.dateMonthTV.setText(month);
            holder.dateDayTV.setText(day);
        }

        // Category
        if (transaction.getCategory() != null ) {
            holder.categoryNameTV.setText(transaction.getCategory().getName());

            holder.hexColorIV.setBackgroundColor(
                    Color.parseColor(transaction.getCategory().getHexColor()));
        }
        // Attachment
        holder.attachmentView.setVisibility(
                transaction.getAttachment() != null && !transaction.getAttachment().equals("") ?
                        View.VISIBLE : View.GONE);


        if (mShowCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.getDateDayLayoutParams().removeRule(RelativeLayout.ALIGN_PARENT_START);
            holder.getDateDayLayoutParams().addRule(RelativeLayout.END_OF, R.id.checkBox);
        } else if (!isCheckBoxHidden) {
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.GONE);
            holder.getDateDayLayoutParams().addRule(RelativeLayout.ALIGN_PARENT_START);
            if (position + 1 == mTransactions.size()) {
                isCheckBoxHidden = true;
            }
        }

        if (mCheckAllCheckBox) {
            holder.checkBox.setChecked(true);
            if (position + 1 == mTransactions.size()) {
                mCheckAllCheckBox = false;
            }
        } else if (mCheckNoneCheckBox) {
            holder.checkBox.setChecked(false);
            if (position + 1 == mTransactions.size()) {
                mCheckNoneCheckBox = false;
            }
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
        isCheckBoxHidden = false;
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
        mSelectedTransactions.clear();
        return arraySelectedTransactionsId;
    }

    void checkAllCheckBoxes(boolean check) {
        Timber.d(mSelectedTransactions.toString());
        
        if (check) {
            for (int i = 0, count = mTransactions.size(); i < count; i++) {
                mSelectedTransactions.append(i, mTransactions.get(i).getId());
            }
            mCheckAllCheckBox = true;
        } else {
            mSelectedTransactions.clear();
            mCheckNoneCheckBox = true;
        }
    }

    // TODO maybe this Holder class leak some memory leaks, try to define it as static inner class
    static class TransactionHolder extends RecyclerView.ViewHolder implements DialogInterface.OnClickListener {
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
        private WeakReference<TransactionAdapter> adapterWR;

        TransactionHolder(View itemView, TransactionAdapter transactionAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            dateDayLayoutParams = (RelativeLayout.LayoutParams) dateDayTV.getLayoutParams();
            adapterWR = new WeakReference<>(transactionAdapter);
        }

        RelativeLayout.LayoutParams getDateDayLayoutParams() {
            return dateDayLayoutParams;
        }

        void setItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        @OnClick(R.id.checkBox)
        void OnClickCheckBox() {
            Timber.i("onClick, item position: %d", itemPosition);
            if (adapterWR == null) {
                Timber.e("adapterWR is null");
                return;
            }
            SparseLongArray selectedTransactions = adapterWR.get().mSelectedTransactions;
            List<Transaction> transactions = adapterWR.get().mTransactions;

            if (checkBox.isChecked()) {
                selectedTransactions.append(itemPosition, transactions.get(itemPosition).getId());
            } else {
                selectedTransactions.delete(itemPosition);
            }
            Timber.i("%s", selectedTransactions.toString());
        }

        // Open the attachment picture.
        @OnClick(R.id.attachment_view)
        void onClickAttachment() {
            Timber.i("onClick, item position: %d", itemPosition);
            if (adapterWR == null) {
                Timber.e("adapterWR is null");
                return;
            }
            Context context = adapterWR.get().mContext;
            List<Transaction> transactions = adapterWR.get().mTransactions;

            if (!PermissionChecker.verifyStoragePermissions((Activity) context)) {
                // TODO check if you can run this code again after the permission allowed.
                return;
            }

            File imageFile = new File(transactions.get(itemPosition).getAttachment());

            if (!imageFile.exists()) {
                DialogFactory.createGenericYesDialog(context,
                        context.getString(R.string.cant_open), this).show();
                return;
            }

            Uri imageURI = FileProvider.getUriForFile(context, MainActivity.FILE_PROVIDER_PATH,
                    imageFile);
            Intent intent = new Intent(Intent.ACTION_VIEW, imageURI);
            // According to FileProvider docs this flag is required.
            // @link https://developer.android.com/reference/android/support/v4/content/FileProvider.html
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }

        // OnClicking Attachment Dialog Yes Button
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (adapterWR == null) {
                Timber.e("adapterWR is null");
                return;
            }
            MainPresenter mainPresenter = adapterWR.get().mMainPresenter;
            List<Transaction> transactions = adapterWR.get().mTransactions;

            mainPresenter.removeAttachmentFromDB(transactions.get(itemPosition).getId());
            dialog.dismiss();
        }
    }
}
