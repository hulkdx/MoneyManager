package com.hulkdx.moneymanagerv2

import android.app.Application
import android.content.Context
import com.hulkdx.moneymanagerv2.di.components.ApplicationComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerApplicationComponent
import com.hulkdx.moneymanagerv2.di.modules.ApplicationModule

import com.squareup.leakcanary.LeakCanary

import timber.log.Timber

/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 * Updated on 23/2/2019.
 */
class HulkApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        initDagger()
        initLeakDetection()
        initTimberLog()
    }

    private fun initDagger() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    private fun initLeakDetection() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            LeakCanary.install(this)
        }
    }

    private fun initTimberLog() {
        // This will initialise Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    // Needed to replace the component with a test specific one
    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }

    companion object {

        operator fun get(context: Context): HulkApplication {
            return context.applicationContext as HulkApplication
        }
    }

}
