package com.hulkdx.moneymanagerv2

import android.app.Application
import android.content.Context

import com.squareup.leakcanary.LeakCanary

import com.hulkdx.moneymanagerv2.di.components.ApplicationComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerApplicationComponent
import com.hulkdx.moneymanagerv2.di.modules.ApplicationModule
import timber.log.Timber

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
class MyApplication : Application() {

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
            LeakCanary.install(this)
        }
    }

    private fun initTimberLog() {
        // This will initialise Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        fun get(context: Context): MyApplication {
            return context.applicationContext as MyApplication
        }
    }
}
