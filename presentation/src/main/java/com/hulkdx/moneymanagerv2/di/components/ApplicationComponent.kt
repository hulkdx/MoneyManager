package com.hulkdx.moneymanagerv2.di.components


import android.app.Application
import android.content.Context
import javax.inject.Singleton

import dagger.Component
import com.hulkdx.moneymanagerv2.di.ApplicationContext
import com.hulkdx.moneymanagerv2.di.modules.ApplicationModule
import com.hulkdx.moneymanagerv2.di.modules.NetworkModule
import com.hulkdx.moneymanagerv2.mapper.GitRepositoryModelMapper
import hulkdx.com.domain.interactor.GetGithubRepositoryList

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun getGithubRepositoryList(): GetGithubRepositoryList
    fun GitRepositoryModelMapper(): GitRepositoryModelMapper
}
