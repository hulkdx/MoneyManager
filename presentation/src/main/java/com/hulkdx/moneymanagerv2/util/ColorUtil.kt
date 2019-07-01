package com.hulkdx.moneymanagerv2.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.hulkdx.moneymanagerv2.R
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 01/07/2019.
 */
class ColorUtil @Inject constructor(context: Context) {

    val mWhiteColor: Int = ContextCompat.getColor(context, R.color.white)
    val mGreyColor: Int = ContextCompat.getColor(context, R.color.grey)

}
