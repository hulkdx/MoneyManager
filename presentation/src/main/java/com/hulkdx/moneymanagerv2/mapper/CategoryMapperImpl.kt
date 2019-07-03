package com.hulkdx.moneymanagerv2.mapper

import com.hulkdx.moneymanagerv2.model.CategoryModel
import com.hulkdx.moneymanagerv2.model.TransactionModel
import hulkdx.com.domain.data.model.Category
import hulkdx.com.domain.data.model.Transaction
import javax.inject.Inject

class CategoryMapperImpl @Inject constructor(): CategoryMapper {

    override fun mapCategory(category: Category?): CategoryModel? {
        return category?.run {
            return@run CategoryModel(id, name, hexColor)
        }
    }

}
