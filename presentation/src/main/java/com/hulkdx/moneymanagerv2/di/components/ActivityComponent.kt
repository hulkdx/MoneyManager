package com.hulkdx.moneymanagerv2.di.components


import com.hulkdx.moneymanagerv2.ViewModelProviderFactory
import hulkdx.com.domain.di.PerActivity
import com.hulkdx.moneymanagerv2.di.modules.ActivityModule
import com.hulkdx.moneymanagerv2.ui.auth.AuthActivity
import com.hulkdx.moneymanagerv2.ui.tutorial.TutorialActivity
import dagger.Subcomponent

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun viewModelProviderFactory(): ViewModelProviderFactory

    fun inject(chooserActivity: TutorialActivity)
    fun inject(authActivity: AuthActivity)
}
