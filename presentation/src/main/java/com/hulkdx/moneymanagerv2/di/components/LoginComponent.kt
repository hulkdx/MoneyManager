package com.hulkdx.moneymanagerv2.di.components

import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.HulkApplication
import hulkdx.com.domain.di.TutorialScope
import com.hulkdx.moneymanagerv2.di.modules.LoginModule
import com.hulkdx.moneymanagerv2.ui.login.LoginFragment
import dagger.BindsInstance
import dagger.Component

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@TutorialScope
@Component(
        modules = [LoginModule::class],
        dependencies = [ApplicationComponent::class]
)
interface LoginComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun fragment(fragment: Fragment): Builder

        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): LoginComponent
    }

    fun inject(loginFragment: LoginFragment)

    companion object {
        fun inject(loginFragment: LoginFragment) {
            DaggerLoginComponent.builder()
                    .fragment(loginFragment)
                    .applicationComponent(HulkApplication.applicationComponent(loginFragment.requireContext()))
                    .build()
                    .inject(loginFragment)
        }
    }
}
