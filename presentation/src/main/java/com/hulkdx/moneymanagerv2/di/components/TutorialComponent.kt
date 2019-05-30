package com.hulkdx.moneymanagerv2.di.components

import android.app.Activity
import com.hulkdx.moneymanagerv2.HulkApplication
import com.hulkdx.moneymanagerv2.di.TutorialScope
import com.hulkdx.moneymanagerv2.di.modules.TutorialModule
import com.hulkdx.moneymanagerv2.ui.tutorial.TutorialActivity
import dagger.BindsInstance
import dagger.Component

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@TutorialScope
@Component(
        modules = [TutorialModule::class],
        dependencies = [ApplicationComponent::class]
)
interface TutorialComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): TutorialComponent
    }
}
