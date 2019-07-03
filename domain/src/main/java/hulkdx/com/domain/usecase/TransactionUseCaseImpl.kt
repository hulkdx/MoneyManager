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
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Singleton
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
                .subscribe({ apiResponse ->
                    when (apiResponse) {
                        is ApiManager.TransactionApiResponse.Success -> {
                            val transactions = apiResponse.transactions
                            val amount = String.format("%.2f", apiResponse.totalAmount)
                            mDatabaseManager.saveTransactions(transactions)
                            onComplete(TransactionResult.Success(transactions, amount, user.currency))
                        }
                        is ApiManager.TransactionApiResponse.GeneralError -> {
                            onComplete(TransactionResult.GeneralError())
                        }
                        is ApiManager.TransactionApiResponse.AuthWrongToken -> {
                            onComplete(TransactionResult.AuthenticationError)
                        }
                    }
                }, {
                    if (it is IOException) {
                        onComplete(TransactionResult.NetworkError(it))
                    } else {
                        onComplete(TransactionResult.GeneralError(it))
                    }
                })
    }

    override fun searchTransactions(searchText: String, onComplete: (TransactionResult) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispose() {
        mDisposable?.apply {
            if (!isDisposed) dispose()
        }
    }

}
