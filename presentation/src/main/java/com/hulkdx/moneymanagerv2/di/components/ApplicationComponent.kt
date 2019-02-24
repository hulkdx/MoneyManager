package com.hulkdx.moneymanagerv2.di.components


import android.app.Application
import android.content.Context
import hulkdx.com.domain.di.ApplicationContext
import com.hulkdx.moneymanagerv2.di.modules.ApplicationModule
import dagger.Component
import hulkdx.com.database.di.DatabaseModule
import hulkdx.com.domain.interactor.AuthUseCase
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Singleton
@Component(modules = [ApplicationModule::class, DatabaseModule::class])
interface ApplicationComponent {

    @ApplicationContext
    fun context():     Context
    fun application(): Application

    fun authUseCase(): AuthUseCase
}
