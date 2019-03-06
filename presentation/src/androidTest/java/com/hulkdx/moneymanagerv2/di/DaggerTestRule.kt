package com.hulkdx.moneymanagerv2.di

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.hulkdx.moneymanagerv2.HulkApplication
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/03/2019.
 */
class DaggerTestRule: TestRule {

    private val mApplication: HulkApplication = HulkApplication.get(ApplicationProvider.getApplicationContext())
    private val mTestComponent: TestComponent = DaggerTestComponent
            .builder()
            .testModule(TestModule(mApplication))
            .build()

    override fun apply(base: Statement?, description: Description?): Statement {
        return object: Statement() {
            override fun evaluate() {
                mApplication.setComponent(mTestComponent)
            }
        }
    }

}