package hulkdx.com.domain.di

import dagger.Module
import dagger.Provides
import hulkdx.com.domain.repository.TransactionRepository
import hulkdx.com.domain.repository.TransactionRepositoryImpl
import hulkdx.com.domain.repository.UserRepository
import hulkdx.com.domain.repository.UserRepositoryImpl
import hulkdx.com.domain.usecase.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Module
object RepositoryModule {

    @JvmStatic
    @Provides
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @JvmStatic
    @Provides
    fun provideTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository = impl

}
