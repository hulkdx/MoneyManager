package com.hulkdx.moneymanagerv2.ui.transaction

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.model.TransactionModel
import com.hulkdx.moneymanagerv2.util.ColorUtil
import javax.inject.Inject

class TransactionListAdapter @Inject constructor(
        private val mColorUtil: ColorUtil
): RecyclerView.Adapter<TransactionListAdapter.TransactionHolder>() {

    var mTransactions = listOf<TransactionModel>()
    var mCurrencyName: String = ""
    var mShowCheckbox = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_transaction, parent, false)
        return TransactionHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = mTransactions[position]

        // Set the background of the layout on odd position to white and on even to grey.
        holder.rootLayout.setBackgroundColor(
                if (position % 2 == 0) mColorUtil.white
                else mColorUtil.grey
        )

        // Attachment
        holder.attachmentView.visibility = transaction.attachmentVisibility

        // Category
        if (transaction.category != null) {
            holder.categoryNameTV.text = transaction.category.name
            holder.hexColorIV.setBackgroundColor(transaction.category.hexColorInt)
        }

        // Set balance text and colors
        holder.balanceNumberTV.text = transaction.balanceNumberText
        holder.balanceNumberTV.setTextColor(transaction.balanceNumberTextColor)
        holder.balanceCurrencyTV.setTextColor(transaction.balanceNumberTextColor)

        // Currency
        holder.balanceCurrencyTV.text = mCurrencyName

        // Date format
        holder.dateMonthTV.text = transaction.dateMonthText
        holder.dateDayTV.text = transaction.dateDayText

        // checkbox
        holder.checkBox.visibility = if (mShowCheckbox) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return mTransactions.size
    }

    // region Extra functions ----------------------------------------------------------------------

    fun checkbox(show: Boolean) {
        mShowCheckbox = show
        notifyDataSetChanged()
    }

    // endregion Extra functions -------------------------------------------------------------------
    // region Holder ----------------------------------------------------------------------

    class TransactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootLayout:        RelativeLayout = itemView.findViewById(R.id.root_layout)
        val balanceNumberTV:   TextView       = itemView.findViewById(R.id.balance_number)
        val balanceCurrencyTV: TextView       = itemView.findViewById(R.id.balance_sign)
        val dateMonthTV:       TextView       = itemView.findViewById(R.id.date_month)
        val dateDayTV:         TextView       = itemView.findViewById(R.id.date_day)
        val categoryNameTV:    TextView       = itemView.findViewById(R.id.category_name_textview)
        val hexColorIV:        ImageView      = itemView.findViewById(R.id.view_hex_color)
        val attachmentView:    View           = itemView.findViewById(R.id.attachment_view)
        val checkBox:          CheckBox       = itemView.findViewById(R.id.checkBox)
    }

    // region Holder ----------------------------------------------------------------------

}
