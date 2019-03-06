package com.hulkdx.moneymanagerv2

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ActivityTestRule
import com.hulkdx.moneymanagerv2.ui.tutorial.TutorialActivity
import org.mockito.Mockito.mock

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/03/2019.
 */

inline fun <reified T: Activity> getTestActivity(): ActivityTestRule<T> {
    return object : ActivityTestRule<T>(T::class.java, false,
            false) {
        override fun getActivityIntent(): Intent = Intent(
                ApplicationProvider.getApplicationContext(), T::class.java)
    }
}

inline fun <reified T> mockIt(): T {
    return mock(T::class.java)
}

fun Int.Companion.random(max: Int): Int {
    return 1
}