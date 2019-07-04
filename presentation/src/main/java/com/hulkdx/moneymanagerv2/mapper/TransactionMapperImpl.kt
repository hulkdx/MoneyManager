package com.hulkdx.moneymanagerv2.mapper

import com.hulkdx.moneymanagerv2.model.TransactionModel
import com.hulkdx.moneymanagerv2.util.ColorUtil
import hulkdx.com.domain.data.model.Transaction
import javax.inject.Inject

class TransactionMapperImpl @Inject constructor(
        private val mCategoryMapper: CategoryMapper,
        private val mColorUtil: ColorUtil
): TransactionMapper {

    override fun mapTransactionList(transactions: List<Transaction>): List<TransactionModel> {

        return transactions.map {
            val category = mCategoryMapper.mapCategory(it.category)
            return@map TransactionModel(
                    it.id,
                    it.date,
                    category,
                    it.amount,
                    it.attachment,
                    mColorUtil.darkGreen,
                    mColorUtil.black
            )
        }
    }

}