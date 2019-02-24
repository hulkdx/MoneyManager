package com.hulkdx.moneymanagerv2.di.modules

import android.app.Application
import android.content.Context
import hulkdx.com.domain.di.ApplicationContext
import com.hulkdx.moneymanagerv2.executor.UiThread
import dagger.Module
import dagger.Provides
import hulkdx.com.repository.datasource.IDataBase
import hulkdx.com.repository.repository.UserRepository
import hulkdx.com.database.DatabaseHelper
import hulkdx.com.domain.executor.CustomThreadExecutor
import hulkdx.com.domain.executor.PostExecutionThread
import hulkdx.com.domain.executor.ThreadExecutor
import hulkdx.com.domain.repository.IUserRepository

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    fun provideApplication(): Application = mApplication

    @Provides
    @ApplicationContext
    fun provideContext(): Context = mApplication

    @Provides
    fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread = uiThread

    @Provides
    fun provideThreadExecutor(customThreadExecutor: CustomThreadExecutor): ThreadExecutor {
        return customThreadExecutor
    }

    @Provides
    fun provideAuthRepository(userRepository: UserRepository): IUserRepository {
        return userRepository
    }

    @Provides
    fun provideDataBase(userRepository: DatabaseHelper): IDataBase {
        return userRepository
    }


}
