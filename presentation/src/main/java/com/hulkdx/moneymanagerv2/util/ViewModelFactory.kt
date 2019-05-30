package com.hulkdx.moneymanagerv2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hulkdx.moneymanagerv2.ui.tutorial.TutorialViewModel
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class ViewModelFactory @Inject constructor(

): ViewModelProvider.Factory
{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == TutorialViewModel::class.java) {
            return TutorialViewModel() as T
        }
        throw RuntimeException("Please add other ViewModels")
    }

}