package com.hulkdx.moneymanagerv2.ui.register

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.widget.Spinner
import androidx.annotation.RequiresApi

/**
 * Created by Mohammad Jafarzadeh Rezvan on 02/06/2019.
 */
class CurrencySpinner: Spinner {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, mode: Int) : super(context, mode)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) : super(context, attrs, defStyleAttr, mode)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int, mode: Int) : super(context, attrs, defStyleAttr, defStyleRes, mode)
    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int, mode: Int, popupTheme: Resources.Theme?) : super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme)
}