package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.database.DatabaseManager
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.RemoteStatus
import hulkdx.com.domain.di.BackgroundScheduler
import hulkdx.com.domain.di.UiScheduler
import hulkdx.com.domain.usecase.AuthUseCase.*
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import org.omg.PortableInterceptor.SUCCESSFUL
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Singleton
class AuthUseCaseImpl @Inject constructor(
        @BackgroundScheduler private val mBackgroundScheduler: Scheduler,
        @UiScheduler         private val mUiScheduler: Scheduler,
                             private val mDatabaseManager: DatabaseManager,
                             private val mApiManager: ApiManager
): AuthUseCase {
    private var mDisposable: Disposable? = null

    override fun loginAsync(username: String,
                            password: String,
                            onComplete: (LoginResult) -> Unit) {
        mDisposable = mApiManager
                .loginSync(username, password)
                .subscribeOn(mBackgroundScheduler)
                .doOnSuccess {
                    if (it.status == RemoteStatus.SUCCESS) {
                        mDatabaseManager.saveUser(it.user)
                    }
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

    override fun registerAsync(firstName: String,
                               lastName:  String,
                               username:  String,
                               password:  String,
                               email:     String,
                               currency:  String,
                               onComplete: (RegisterResult) -> Unit) {
        mDisposable = mApiManager
                .registerSync(firstName, lastName, username, password, email, currency)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mUiScheduler)
                .subscribe({
                    when (it.status) {
                        RemoteStatus.SUCCESS -> onComplete(RegisterResult.Successful)
                        RemoteStatus.AUTH_ERROR -> {
                            onComplete(RegisterResult.AuthError)
                        }
                        RemoteStatus.NETWORK_ERROR -> onComplete(RegisterResult.NetworkError())
                        RemoteStatus.GENERAL_ERROR -> onComplete(RegisterResult.GeneralError())
                    }
                }, {
                    if (it is IOException) {
                        onComplete(RegisterResult.NetworkError(it))
                    } else {
                        onComplete(RegisterResult.GeneralError(it))
                    }
                })
    }

    override fun dispose() {
        mDisposable?.apply {
            if (!isDisposed) dispose()
        }
    }

}
