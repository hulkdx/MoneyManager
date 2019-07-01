package com.hulkdx.moneymanagerv2.ui.transaction

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.util.ColorUtil
import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.di.MainActivityScope
import javax.inject.Inject

class TransactionListAdapter @Inject constructor(
        val mColorUtil: ColorUtil
): RecyclerView.Adapter<TransactionListAdapter.TransactionHolder>() {

    var mTransactions = listOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_transaction, parent, false)
        return TransactionHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val (id, date, category, amount, attachment) = mTransactions[position]

        // Set the background of the layout on odd position to white and on even to grey.
        holder.rootLayout.setBackgroundColor(if (position % 2 == 0) mColorUtil.mWhiteColor else mColorUtil.mGreyColor)

        // Attachment
        holder.attachmentView.visibility =
                if (attachment != null && attachment != "")
                    View.VISIBLE
                else
                    View.GONE

        // Category
        if (category != null) {
            holder.categoryNameTV.text = category.name
            // TODO use ColorUtil
            holder.hexColorIV.setBackgroundColor(
                    Color.parseColor(category.hexColor))
        }

        // TODO add this logic in UseCase
        if (amount > 0) {
        }
    }

    override fun getItemCount(): Int {
        return mTransactions.size
    }

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
}
