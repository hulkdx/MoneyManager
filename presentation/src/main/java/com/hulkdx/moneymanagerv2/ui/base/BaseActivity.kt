package com.hulkdx.moneymanagerv2.ui.base

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hulkdx.moneymanagerv2.HulkApplication
import com.hulkdx.moneymanagerv2.di.components.ActivityComponent
import com.hulkdx.moneymanagerv2.di.components.ApplicationComponent
import com.hulkdx.moneymanagerv2.utils.ConfigPersistentHelper

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity : AppCompatActivity() {

    lateinit var mApplicationComponent: ApplicationComponent
    lateinit var mActivityComponent: ActivityComponent
        private set

    private val mConfigPersistentHelper = ConfigPersistentHelper()

    //---------------------------------------------------------------
    // Lifecycle
    //---------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApplicationComponent = HulkApplication.get(this).applicationComponent
        mConfigPersistentHelper.create(savedInstanceState, this)
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
        mConfigPersistentHelper.remove()
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
