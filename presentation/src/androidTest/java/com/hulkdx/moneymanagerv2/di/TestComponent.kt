package com.hulkdx.moneymanagerv2.di

import com.hulkdx.moneymanagerv2.di.components.ApplicationComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/03/2019.
 */
@Singleton
@Component(modules = [TestModule::class])
interface TestComponent: ApplicationComponent {

}