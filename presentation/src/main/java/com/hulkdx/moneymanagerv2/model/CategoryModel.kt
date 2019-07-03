package com.hulkdx.moneymanagerv2.model

import android.graphics.Color

data class CategoryModel (
        val id: Long,
        val name: String,
        val hexColorString: String
) {
    val hexColorInt: Int = Color.parseColor(hexColorString)
}