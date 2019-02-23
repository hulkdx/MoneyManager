package com.hulkdx.moneymanagerv2.ui.base

import android.os.Build
import android.os.Bundle
import android.util.LongSparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.MyApplication
import com.hulkdx.moneymanagerv2.di.components.ActivityComponent
import com.hulkdx.moneymanagerv2.di.components.ApplicationComponent
import com.hulkdx.moneymanagerv2.di.components.ConfigPersistentComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerConfigPersistentComponent
import com.hulkdx.moneymanagerv2.di.modules.ActivityModule
import timber.log.Timber

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val ACTIVITY_ID = "ACTIVITY_ID"

        private var sActivityId = 0
        private val sComponentsMap = LongSparseArray<ConfigPersistentComponent>()
    }

    lateinit var activityComponent: ActivityComponent
        private set

    private val applicationComponent: ApplicationComponent
        get() = MyApplication.get(this).applicationComponent

    //---------------------------------------------------------------
    // Lifecycle
    //---------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createConfigPersistentActivityComponent(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        onBindUI()
    }

    override fun onStop() {
        super.onStop()
        //
        // onStop is lazy on Nougat+
        // https://www.bignerdranch.com/blog/android-activity-lifecycle-onStop/
        //
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            onUnbindUI()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            onUnbindUI()
        }
        if (isFinishing) {
            onDestroyWithoutConfigurationChange()
        }
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            onDestroyWithoutConfigurationChange()
        }
        super.onDestroy()
    }

    protected fun onDestroyWithoutConfigurationChange() {
        removeConfigPersistentComponent()
    }

    // Update UI elements on this function:
    protected fun onBindUI() {}

    // Stop updating UI elements on this function:
    protected fun onUnbindUI() {}

    //---------------------------------------------------------------
    // Config Persistent Component
    //---------------------------------------------------------------

    /**
     * Create ActivityComponent that can survive a configuration using a static HashMap. (In memory)
     */
    private fun createConfigPersistentActivityComponent(savedInstanceState: Bundle?) {
        sActivityId = savedInstanceState?.getInt(ACTIVITY_ID) ?: sActivityId + 1

        val configPersistentComponent: ConfigPersistentComponent =
                sComponentsMap.get(sActivityId.toLong(), null) ?:
                createNewConfigPersistentComponent()

        activityComponent = configPersistentComponent.activityComponent(ActivityModule(this))
    }

    private fun createNewConfigPersistentComponent(): ConfigPersistentComponent {
        Timber.v("Creating new ConfigPersistentComponent")
        val configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
        sComponentsMap.put(sActivityId.toLong(), configPersistentComponent)
        return configPersistentComponent
    }

    private fun removeConfigPersistentComponent() {
        Timber.v("Clearing Component id=%d", sActivityId)
        sComponentsMap.remove(sActivityId.toLong())
    }

    //---------------------------------------------------------------
    // Fragments
    //---------------------------------------------------------------

    protected fun addFragment(containerViewId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(containerViewId, fragment).commit()
    }

}
