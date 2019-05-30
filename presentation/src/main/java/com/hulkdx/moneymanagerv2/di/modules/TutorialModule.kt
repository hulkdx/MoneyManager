package com.hulkdx.moneymanagerv2.di.modules

import androidx.fragment.app.FragmentActivity
import com.hulkdx.moneymanagerv2.ui.tutorial.TutorialViewModel
import androidx.lifecycle.ViewModelProviders
import com.hulkdx.moneymanagerv2.util.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named


/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
abstract class TutorialModule {
    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideTutorialViewModel(fragmentActivity: FragmentActivity,
                                     factory: ViewModelFactory): TutorialViewModel {
            return ViewModelProviders.of(fragmentActivity, factory).get(TutorialViewModel::class.java)
        }
    }

}
