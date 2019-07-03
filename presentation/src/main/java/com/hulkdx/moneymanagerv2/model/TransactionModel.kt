package com.hulkdx.moneymanagerv2.model

import android.view.View
import java.text.DateFormatSymbols

class TransactionModel (
        id: Long,
        date: String,
        val category: CategoryModel?,
        amount: Float,
        attachment: String?,
        balanceNumberPositiveColor: Int,
        balanceNumberNegativeColor: Int,
        val currencyName: String
) {

    val attachmentVisibility: Int =
            if (attachment != null && attachment != "")
                View.VISIBLE
            else
                View.GONE

    val balanceNumberText: String =
            if (amount > 0)
                String.format("+ %.2f", amount)
            else
                String.format("- %.2f", amount)

    val balanceNumberTextColor: Int =
            if (amount > 0)
                balanceNumberPositiveColor
            else
                balanceNumberNegativeColor

    val dateMonthText: String
    val dateDayText: String

    init {
        val splitDate = date.split("-")
        if (splitDate.size == 3) {
            dateMonthText = DateFormatSymbols().shortMonths[Integer.parseInt(splitDate[1]) - 1]
            dateDayText = splitDate[2]
        } else {
            dateMonthText = ""
            dateDayText = ""
        }
    }
}