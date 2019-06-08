package com.hulkdx.moneymanagerv2.di.components

import androidx.fragment.app.Fragment
import hulkdx.com.domain.di.TutorialScope
import com.hulkdx.moneymanagerv2.ui.tutorial.LoginFragment
import dagger.BindsInstance
import dagger.Component

/**
 * @See LoginFragment
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@TutorialScope
@Component(modules = [
], dependencies = [ApplicationComponent::class])
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
