package com.hulkdx.moneymanagerv2.ui.base

import android.os.Build
import android.os.Bundle
import android.util.LongSparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.HulkApplication
import com.hulkdx.moneymanagerv2.di.components.ActivityComponent
import com.hulkdx.moneymanagerv2.di.components.ApplicationComponent
import com.hulkdx.moneymanagerv2.di.components.ConfigPersistentComponent
import com.hulkdx.moneymanagerv2.di.components.DaggerConfigPersistentComponent
import com.hulkdx.moneymanagerv2.di.modules.ActivityModule
import com.hulkdx.moneymanagerv2.utils.ConfigPersistentHelper
import timber.log.Timber

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity : AppCompatActivity() {

    lateinit var applicationComponent: ApplicationComponent
    lateinit var activityComponent: ActivityComponent
        private set

    private val configPersistentHelper = ConfigPersistentHelper()

    //---------------------------------------------------------------
    // Lifecycle
    //---------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationComponent = HulkApplication.get(this).applicationComponent
        configPersistentHelper.create(savedInstanceState, this)
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
        configPersistentHelper.remove()
    }

    // Update UI elements on this function:
    protected fun onBindUI() {}

    // Stop updating UI elements on this function:
    protected fun onUnbindUI() {}

    //---------------------------------------------------------------
    // Fragments
    //---------------------------------------------------------------

    protected fun addFragment(containerViewId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(containerViewId, fragment).commit()
    }

}
