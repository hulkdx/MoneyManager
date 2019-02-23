package com.hulkdx.moneymanagerv2.di.components


import dagger.Subcomponent
import com.hulkdx.moneymanagerv2.di.PerActivity
import com.hulkdx.moneymanagerv2.di.modules.ActivityModule
import com.hulkdx.moneymanagerv2.ui.main.MainActivity
import com.hulkdx.moneymanagerv2.ui.main.MainFragment

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: MainActivity)
    fun inject(mainFragment: MainFragment)
}
