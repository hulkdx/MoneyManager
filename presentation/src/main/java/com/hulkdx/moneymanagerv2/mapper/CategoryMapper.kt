package com.hulkdx.moneymanagerv2.mapper

import com.hulkdx.moneymanagerv2.model.CategoryModel
import com.hulkdx.moneymanagerv2.model.TransactionModel
import hulkdx.com.domain.data.model.Category
import hulkdx.com.domain.data.model.Transaction

interface CategoryMapper {
    fun mapCategory(category: Category?): CategoryModel?
}