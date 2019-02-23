package com.hulkdx.moneymanagerv2.utils

import android.app.Activity
import android.os.Bundle
import android.util.LongSparseArray
import com.hulkdx.moneymanagerv2.HulkApplication
import com.hulkdx.moneymanagerv2.di.components.ActivityComponent
import com.hulkdx.moneymanagerv2.di.components.ConfigPersistentComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerConfigPersistentComponent
import com.hulkdx.moneymanagerv2.di.modules.ActivityModule
import timber.log.Timber

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
class ConfigPersistentHelper {

    companion object {
        private const val ACTIVITY_ID = "ACTIVITY_ID"

        private var sActivityId = 0
        private val sComponentsMap = LongSparseArray<ConfigPersistentComponent>()
    }

    /**
     * Create ActivityComponent that can survive a configuration using a static HashMap. (In memory)
     */
    fun create(savedInstanceState: Bundle?, activity: Activity): ActivityComponent {
        sActivityId = savedInstanceState?.getInt(ACTIVITY_ID) ?: sActivityId + 1

        val configPersistentComponent: ConfigPersistentComponent =
                sComponentsMap.get(sActivityId.toLong(), null) ?:
                newComponent(activity)

        return configPersistentComponent.activityComponent(ActivityModule(activity))
    }

    private fun newComponent(activity: Activity): ConfigPersistentComponent {
        Timber.v("Creating new ConfigPersistentComponent")
        val configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(HulkApplication.get(activity).applicationComponent)
                .build()
        sComponentsMap.put(sActivityId.toLong(), configPersistentComponent)
        return configPersistentComponent
    }

    public fun remove() {
        Timber.v("Clearing Component id=%d", sActivityId)
        sComponentsMap.remove(sActivityId.toLong())
    }

}