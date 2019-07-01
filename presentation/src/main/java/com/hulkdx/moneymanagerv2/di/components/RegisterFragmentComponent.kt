package com.hulkdx.moneymanagerv2.di.components

import android.content.Context
import androidx.fragment.app.Fragment
import hulkdx.com.domain.di.MainActivityScope
import com.hulkdx.moneymanagerv2.ui.register.RegisterFragment
import dagger.BindsInstance
import dagger.Component

/**
 * @See RegisterFragment
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@MainActivityScope
@Component(modules = [
], dependencies = [ApplicationComponent::class])
interface RegisterFragmentComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun fragment(fragment: Fragment): Builder
        @BindsInstance
        fun context(context: Context): Builder
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): RegisterFragmentComponent
    }

    fun inject(registerFragment: RegisterFragment)
}
