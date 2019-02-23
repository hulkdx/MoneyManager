/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanagerv2.di.components

import dagger.Component
import com.hulkdx.moneymanagerv2.di.ConfigPersistent
import com.hulkdx.moneymanagerv2.di.modules.ActivityModule
import com.hulkdx.moneymanagerv2.di.modules.ConfigPersistentModule

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */

@ConfigPersistent
@Component(dependencies = [ApplicationComponent::class],
           modules = [ConfigPersistentModule::class])
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

}