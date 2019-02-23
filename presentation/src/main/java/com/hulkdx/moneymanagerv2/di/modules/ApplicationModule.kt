package com.hulkdx.moneymanagerv2.di.modules

import android.app.Application
import android.content.Context

import dagger.Module
import dagger.Provides
import com.hulkdx.moneymanagerv2.di.ApplicationContext
import com.hulkdx.moneymanagerv2.executor.UiThread
import hulkdx.com.domain.executor.CustomThreadExecutor
import hulkdx.com.domain.executor.PostExecutionThread
import hulkdx.com.domain.executor.ThreadExecutor
import hulkdx.com.domain.repository.GithubRepoRepository
import hulkdx.com.data.repository.GithubRepoRepositoryImpl

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
    fun provideGithubRepoRepository(impl: GithubRepoRepositoryImpl): GithubRepoRepository {
        return impl
    }
}
