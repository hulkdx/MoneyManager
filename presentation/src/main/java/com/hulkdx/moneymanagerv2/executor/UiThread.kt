package com.hulkdx.moneymanagerv2.executor

import javax.inject.Inject
import javax.inject.Singleton

import hulkdx.com.domain.executor.PostExecutionThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
@Singleton
class UiThread @Inject
constructor() : PostExecutionThread {

    override fun getScheduler(): Scheduler = AndroidSchedulers.mainThread()

}
