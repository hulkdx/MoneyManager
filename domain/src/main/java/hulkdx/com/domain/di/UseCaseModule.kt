package hulkdx.com.domain.di

import dagger.Module
import dagger.Provides
import hulkdx.com.domain.usecase.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Module
object UseCaseModule {
    @JvmStatic
    @Provides
    fun provideAuthUseCase(authUseCaseImpl: AuthUseCaseImpl): AuthUseCase = authUseCaseImpl

    @JvmStatic
    @Provides
    fun provideTransactionUseCase(useCase: TransactionUseCaseImpl): TransactionUseCase = useCase

    @JvmStatic
    @Provides
    fun provideTransactionCategoryUseCase(useCase: TransactionCategoryUseCaseImpl)
            : TransactionCategoryUseCase = useCase
}
