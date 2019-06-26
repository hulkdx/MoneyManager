package hulkdx.com.domain.di

import dagger.Module
import dagger.Provides
import hulkdx.com.domain.usecase.AuthUseCase
import hulkdx.com.domain.usecase.AuthUseCaseImpl

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Module
object UseCaseModule {
    @JvmStatic
    @Provides
    fun provideAuthUseCase(authUseCaseImpl: AuthUseCaseImpl): AuthUseCase = authUseCaseImpl
}
