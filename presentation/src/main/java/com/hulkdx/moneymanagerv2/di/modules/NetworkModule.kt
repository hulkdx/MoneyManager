package com.hulkdx.moneymanagerv2.di.modules

import dagger.Module
import dagger.Provides
import hulkdx.com.data.remote.RemoteService
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRemoteService(): RemoteService = RemoteService.Factory.create()
}
