package com.hulkdx.moneymanagerv2

import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hulkdx.moneymanagerv2.di.PerActivity
import com.hulkdx.moneymanagerv2.ui.main.MainViewModel
import java.lang.RuntimeException

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/02/2019.
 */
@PerActivity
class ViewModelProviderFactory @Inject constructor() : ViewModelProvider.Factory {

    @Inject lateinit var mMainViewModel: MainViewModel

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == MainViewModel::class.java) {
            return mMainViewModel as T
        }

        throw RuntimeException("Please add other ViewModels")
    }
}
