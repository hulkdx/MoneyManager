package hulkdx.com.domain.di

import dagger.Module
import dagger.Provides
import hulkdx.com.domain.usecase.LoginUseCase
import hulkdx.com.domain.usecase.LoginUseCaseImpl
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Module
class UseCaseModule {
    @Module
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase = loginUseCaseImpl
    }
}