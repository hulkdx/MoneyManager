package com.hulkdx.moneymanagerv2

import android.app.Application
import android.content.Context
import com.hulkdx.moneymanagerv2.di.components.ApplicationComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */

// region Statics -------------------------------------------------------------------------------

fun applicationComponent(context: Context): ApplicationComponent {
    return (context.applicationContext as MoneyManagerApplication).applicationComponent
}

// endregion Statics --------------------------------------------------------------------------

class MoneyManagerApplication : Application() {

    internal lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        initDagger()
        initLeakDetection()
    }

    private fun initDagger() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationContext(this)
                .build()
    }

    private fun initLeakDetection() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return
            }
            LeakCanary.install(this)
        }
    }

}
