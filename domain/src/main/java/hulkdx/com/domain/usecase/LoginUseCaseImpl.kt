package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.RemoteStatus
import hulkdx.com.domain.di.BackgroundScheduler
import hulkdx.com.domain.di.UiScheduler
import hulkdx.com.domain.usecase.LoginUseCase.*
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Singleton
class LoginUseCaseImpl @Inject constructor(
        @BackgroundScheduler private val mBackgroundScheduler: Scheduler,
        @UiScheduler         private val mUiScheduler: Scheduler,
                             private val mApiManager: ApiManager
): LoginUseCase {

    private var mDisposable: Disposable? = null

    override fun loginAsync(username: String,
                            password: String,
                            onComplete: (LoginResult) -> Unit) {
        mDisposable = mApiManager
                .loginSync(username, password)
                .subscribeOn(mBackgroundScheduler)
                .doOnSuccess {
                    // TODO save it into database.
                }
                .observeOn(mUiScheduler)
                .subscribe({
                    onComplete(LoginResult(it.status))
                }, {
                    if (it is IOException) {
                        onComplete(LoginResult(RemoteStatus.NETWORK_ERROR, throwable = it))
                    } else {
                        onComplete(LoginResult(RemoteStatus.GENERAL_ERROR, throwable = it))
                    }
                })
    }

    override fun dispose() {
        mDisposable?.apply {
            if (!isDisposed) dispose()
        }
    }

}
