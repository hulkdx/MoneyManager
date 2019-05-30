package com.hulkdx.moneymanagerv2.di.modules

import dagger.Module
import dagger.Provides
import hulkdx.com.domain.di.BackgroundScheduler
import hulkdx.com.domain.di.UiScheduler
import hulkdx.com.domain.util.CustomThreadExecutor
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
abstract class ApplicationModule {
    @Module
    companion object {
        @JvmStatic
        @UiScheduler
        @Provides
        fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

        @JvmStatic
        @BackgroundScheduler
        @Provides
        fun provideBackgroundScheduler(customThreadExecutor: CustomThreadExecutor): Scheduler =
                Schedulers.from(customThreadExecutor)
    }
}
