package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.manager.DataSourceManager
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.di.BackgroundScheduler
import hulkdx.com.domain.di.UiScheduler
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class TransactionUseCaseImpl @Inject constructor(
        @BackgroundScheduler private val mBackgroundScheduler: Scheduler,
        @UiScheduler         private val mUiScheduler: Scheduler,
                             private val mDatabaseManager: DatabaseManager,
                             private val mCacheManager: CacheManager,
                             private val mApiManager: ApiManager,
                             private val mDataSourceManager: DataSourceManager
): TransactionUseCase {

    private var mDisposable: Disposable? = null

    override fun getTransactions(onComplete: (TransactionResult) -> Unit) {
        val user = mDataSourceManager.getUser()
        if (user == null) {
            // Auth error!
            onComplete(TransactionResult.AuthenticationError)
            return
        }
        mDisposable = mApiManager.getTransactions(user.token)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mUiScheduler)
                .subscribe({

                }, {

                })
    }

    override fun dispose() {
        mDisposable?.apply {
            if (!isDisposed) dispose()
        }
    }

}
