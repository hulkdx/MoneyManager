package com.hulkdx.moneymanagerv2.ui.tutorial

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.di.DaggerTestRule
import com.hulkdx.moneymanagerv2.getTestActivity
import org.junit.Rule
import org.junit.rules.RuleChain


/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/03/2019.
 */
@RunWith(AndroidJUnit4::class)
class TutorialActivityTest {

    private val activityRule: ActivityTestRule<TutorialActivity> = getTestActivity()
    private val daggerTestRule = DaggerTestRule()

    // This rule make sure that dagger applies before activityRule running.
    @get:Rule
    val ruleChain = RuleChain.outerRule(daggerTestRule).around(activityRule)!!

    @Before
    fun setup() {
    }

    @Test
    fun currentStepInitialIsZero() {
        activityRule.launchActivity(null)
        assertEquals(TutorialActivity.currentStep, WelcomeFragment.STEP_ID)
    }

    @Test
    fun currentStepReplaceSyncTest() {
        activityRule.launchActivity(null)
        activityRule.activity.replaceFragment(R.id.container, LoginFragment.newInstance(true))

        assertEquals(TutorialActivity.currentStep, LoginFragment.STEP_ID_SYNC)
    }

    @Test
    fun currentStepBackBtnTest() {
        activityRule.launchActivity(null)
        activityRule.activity.replaceFragment(R.id.container, LoginFragment.newInstance(true))
        activityRule.activity.onBackPressed()

        assertEquals(TutorialActivity.currentStep, WelcomeFragment.STEP_ID)
    }


    @Test
    fun currentStepReplaceNonSyncTest() {
        activityRule.launchActivity(null)
        activityRule.activity.replaceFragment(R.id.container, LoginFragment.newInstance(false))

        assertEquals(TutorialActivity.currentStep, LoginFragment.STEP_ID_NON_SYNC)
    }
}