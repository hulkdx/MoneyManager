package com.hulkdx.moneymanagerv2.di.modules

import com.hulkdx.moneymanagerv2.mapper.CategoryMapper
import com.hulkdx.moneymanagerv2.mapper.CategoryMapperImpl
import com.hulkdx.moneymanagerv2.mapper.TransactionMapper
import com.hulkdx.moneymanagerv2.mapper.TransactionMapperImpl
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
object MapperModule {

    @JvmStatic
    @Provides
    fun provideTransactionMapper(transactionMapperImpl: TransactionMapperImpl): TransactionMapper =
            transactionMapperImpl

    @JvmStatic
    @Provides
    fun provideCategoryMapper(impl: CategoryMapperImpl): CategoryMapper = impl

}
