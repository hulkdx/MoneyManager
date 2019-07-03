package com.hulkdx.moneymanagerv2.mapper

import com.hulkdx.moneymanagerv2.model.TransactionModel
import hulkdx.com.domain.data.model.Transaction

interface TransactionMapper {
    fun mapTransactionList(transactions: List<Transaction>,
                           currencyName: String): List<TransactionModel>
}