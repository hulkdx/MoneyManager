package com.hulkdx.moneymanagerv2.di

import android.app.Application
import android.content.Context
import com.hulkdx.moneymanagerv2.executor.UiThread
import com.hulkdx.moneymanagerv2.mockIt
import dagger.Module
import hulkdx.com.domain.di.ApplicationContext
import dagger.Provides
import hulkdx.com.database.DatabaseHelper
import hulkdx.com.domain.executor.CustomThreadExecutor
import hulkdx.com.domain.executor.PostExecutionThread
import hulkdx.com.domain.executor.ThreadExecutor
import hulkdx.com.domain.repository.IUserRepository
import hulkdx.com.repository.datasource.IDataBase
import hulkdx.com.repository.datasource.IMemoryCache
import hulkdx.com.repository.datasource.MemoryCache
import hulkdx.com.repository.repository.UserRepository
import io.realm.Realm
import io.realm.RealmConfiguration
import org.mockito.Mockito.mock
import javax.inject.Singleton


/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/03/2019.
 */
@Module
class TestModule constructor(private val mApplication: Application) {

    @Provides
    fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return mApplication
    }

    //---------------------
    // Mocks
    //---------------------

    @Provides
    fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread = mockIt()

    @Provides
    fun provideThreadExecutor(customThreadExecutor: CustomThreadExecutor): ThreadExecutor {
        return mockIt()
    }

    @Provides
    fun provideAuthRepository(userRepository: UserRepository): IUserRepository {
        return mockIt()
    }

    @Provides
    fun provideDataBase(userRepository: DatabaseHelper): IDataBase {
        return mockIt()
    }

    @Provides
    fun provideMemoryCache(memoryCache: MemoryCache): IMemoryCache {
        return mockIt()
    }

    @Provides
    @Singleton
    fun provideRealm(realmConfiguration: RealmConfiguration): Realm {
        return mockIt()
    }

    @Provides
    @Singleton
    fun provideRealmConfiguration(@ApplicationContext context: Context): RealmConfiguration {
        return mockIt()
    }
}