package com.hulkdx.moneymanagerv2.di.components

import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.applicationComponent
import hulkdx.com.domain.di.TutorialScope
import com.hulkdx.moneymanagerv2.di.modules.LoginModule
import com.hulkdx.moneymanagerv2.ui.login.LoginFragment
import dagger.BindsInstance
import dagger.Component

/**
 * @See LoginFragment
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */

// region Statics -------------------------------------------------------------------------------

fun inject(loginFragment: LoginFragment) {
    DaggerLoginFragmentComponent.builder()
            .fragment(loginFragment)
            .applicationComponent(applicationComponent(loginFragment.requireContext()))
            .build()
            .inject(loginFragment)
}

// endregion Statics -------------------------------------------------------------------------------

@TutorialScope
@Component(
        modules = [LoginModule::class],
        dependencies = [ApplicationComponent::class]
)
interface LoginFragmentComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun fragment(fragment: Fragment): Builder

        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): LoginFragmentComponent
    }

    fun inject(loginFragment: LoginFragment)
}
