package com.hulkdx.moneymanagerv2.di.components


import android.app.Application
import android.content.Context
import com.hulkdx.moneymanagerv2.di.ApplicationContext
import com.hulkdx.moneymanagerv2.di.modules.ApplicationModule
import com.hulkdx.moneymanagerv2.di.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {

    @ApplicationContext
    fun context():     Context
    fun application(): Application

}
