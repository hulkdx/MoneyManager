package com.hulkdx.moneymanagerv2.di.components

import android.content.Context
import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.di.modules.MapperModule
import hulkdx.com.domain.di.MainActivityScope
import com.hulkdx.moneymanagerv2.ui.login.LoginFragment
import com.hulkdx.moneymanagerv2.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component

/**
 * @See LoginFragment
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@MainActivityScope
@Component(modules = [
    MapperModule::class
], dependencies = [ApplicationComponent::class])
interface MainActivityComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): MainActivityComponent
    }

    fun inject(mainActivity: MainActivity)
}
