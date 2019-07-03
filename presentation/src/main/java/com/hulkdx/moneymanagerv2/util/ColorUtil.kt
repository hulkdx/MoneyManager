package com.hulkdx.moneymanagerv2.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.hulkdx.moneymanagerv2.R
import hulkdx.com.domain.di.MainActivityScope
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 01/07/2019.
 */
@MainActivityScope
class ColorUtil @Inject constructor(context: Context) {

    val white: Int = ContextCompat.getColor(context, R.color.white)
    val grey: Int = ContextCompat.getColor(context, R.color.grey)
    val darkGreen: Int = ContextCompat.getColor(context, R.color.darkgreen)
    val black: Int = ContextCompat.getColor(context, R.color.black)

}
