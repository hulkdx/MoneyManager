package com.hulkdx.moneymanagerv2.di.components

import com.hulkdx.moneymanagerv2.di.modules.ApplicationModule
import dagger.Component
import hulkdx.com.data.remote.di.NetworkModule
import hulkdx.com.domain.di.UseCaseModule
import hulkdx.com.domain.usecase.LoginUseCase
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Singleton
@Component(modules = [
    ApplicationModule::class,
    UseCaseModule::class,
    NetworkModule::class
])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
    }

    fun loginUseCase(): LoginUseCase
}
