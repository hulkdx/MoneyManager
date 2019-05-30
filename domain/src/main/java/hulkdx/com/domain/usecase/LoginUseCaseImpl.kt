package hulkdx.com.domain.usecase

import hulkdx.com.domain.di.BackgroundScheduler
import hulkdx.com.domain.di.UiScheduler
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Singleton
class LoginUseCaseImpl @Inject constructor(
        @BackgroundScheduler private val mBackgroundScheduler: Scheduler,
        @UiScheduler         private val mUiScheduler: Scheduler
): LoginUseCase {

    override fun loginAsync(username: String,
                            password: String,
                            onComplete: (LoginUseCase.LoginResult) -> Unit) {
    }

}
