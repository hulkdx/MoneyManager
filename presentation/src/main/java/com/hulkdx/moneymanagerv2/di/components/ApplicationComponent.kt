package com.hulkdx.moneymanagerv2.di.components

import android.content.Context
import com.hulkdx.moneymanagerv2.di.modules.ApplicationModule
import dagger.BindsInstance
import dagger.Component
import hulkdx.com.data.di.DatabaseModule
import hulkdx.com.data.remote.di.NetworkModule
import hulkdx.com.domain.di.ApplicationContext
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
    DatabaseModule::class,
    NetworkModule::class
])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        @ApplicationContext
        fun applicationContext(context: Context): Builder
        fun build(): ApplicationComponent
    }

    fun loginUseCase(): LoginUseCase
}
