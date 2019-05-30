package hulkdx.com.domain.di

import dagger.Module
import dagger.Provides
import hulkdx.com.domain.usecase.LoginUseCase
import hulkdx.com.domain.usecase.LoginUseCaseImpl

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Module
class UseCaseModule {
    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase = loginUseCaseImpl
    }
}