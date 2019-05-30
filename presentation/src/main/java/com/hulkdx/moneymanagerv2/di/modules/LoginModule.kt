package com.hulkdx.moneymanagerv2.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hulkdx.moneymanagerv2.ui.login.LoginViewModel
import androidx.lifecycle.ViewModelProviders
import com.hulkdx.moneymanagerv2.util.ViewModelFactory
import dagger.Module
import dagger.Provides


/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
abstract class LoginModule {
    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideTutorialViewModel(fragment: Fragment,
                                     factory: ViewModelFactory): LoginViewModel {
            return ViewModelProviders.of(fragment, factory).get(LoginViewModel::class.java)
        }
    }

}
