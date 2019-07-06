package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.ApiManager.TransactionApiResponse
import hulkdx.com.domain.di.BackgroundScheduler
import hulkdx.com.domain.di.UiScheduler
import hulkdx.com.domain.repository.TransactionRepository
import hulkdx.com.domain.repository.UserRepository
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
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
                             private val mApiManager: ApiManager,
                             private val mUserRepository: UserRepository,
                             private val mTransactionRepository: TransactionRepository
): TransactionUseCase {

    private var mDisposables: CompositeDisposable = CompositeDisposable()

    override fun getTransactionsAsync(onComplete: (TransactionResult) -> Unit) {
        val user = mUserRepository.getCurrentUser()
        if (user == null) {
            // Auth error!
            onComplete(TransactionResult.AuthenticationError)
            return
        }
        val disposable = mApiManager.getTransactions(user.token)
                .subscribeOn(mBackgroundScheduler)
                .saveTransactions()
                .observeOn(mUiScheduler)
                .subscribe({ apiResponse ->
                    when (apiResponse) {
                        is TransactionApiResponse.Success -> {
                            val transactions = apiResponse.transactions
                            val amount = String.format("%.2f", apiResponse.totalAmount)
                            onComplete(TransactionResult.Success(transactions, amount, user.currency))
                        }
                        is TransactionApiResponse.GeneralError -> {
                            onComplete(TransactionResult.GeneralError())
                        }
                        is TransactionApiResponse.AuthWrongToken -> {
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

        mDisposables.add(disposable)
    }

    override fun searchTransactionsAsync(searchText: String, onComplete: (List<Transaction>) -> Unit) {
        val disposable = Single.fromCallable { searchTransactions(searchText) }
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mUiScheduler)
                .subscribe({
                    onComplete(it)
                }, {
                    // ignore, retry?
                })
        mDisposables.add(disposable)
    }

    private fun searchTransactions(searchText: String): List<Transaction> {
        if (searchText.isEmpty()) {
            return mTransactionRepository.findAll()
        }

        val isSearchTextNumber = searchText.matches(Regex("^-?\\d+.?(\\d+)?$"))

        return if (isSearchTextNumber) {
            mTransactionRepository.findByAbsoluteAmount(searchText.toFloat())
        } else {
            mTransactionRepository.findByCategoryName(searchText)
        }
    }

    // TODO if deleting from the api succeed and deleting from db is not.
    override fun deleteTransactionsAsync(id: List<Long>, onComplete: (TransactionResult) -> Unit) {
        val user = mUserRepository.getCurrentUser()
        if (user == null) {
            // Auth error!
            onComplete(TransactionResult.AuthenticationError)
            return
        }
        val disposable = mApiManager.deleteTransactions(user.token, id)
                .subscribeOn(mBackgroundScheduler)
                .deleteTransactions(id)
                .observeOn(mUiScheduler)
                .subscribe({ apiResponse ->
                    when (apiResponse) {
                        is TransactionApiResponse.Success -> {
                            val transactions = apiResponse.transactions
                            val amount = String.format("%.2f", apiResponse.totalAmount)
                            onComplete(TransactionResult.Success(transactions, amount, user.currency))
                        }
                        is TransactionApiResponse.GeneralError -> {
                            onComplete(TransactionResult.GeneralError())
                        }
                        is TransactionApiResponse.AuthWrongToken -> {
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

        mDisposables.add(disposable)
    }

    // region Extra --------------------------------------------------------------------------------

    override fun dispose() {
        mDisposables.clear()
    }

    private fun Single<TransactionApiResponse>.saveTransactions(): Single<TransactionApiResponse> {
        return doOnSuccess { apiResponse ->
            if (apiResponse is TransactionApiResponse.Success) {
                mTransactionRepository.save(apiResponse.transactions)
            }
        }
    }

    private fun Single<TransactionApiResponse>.deleteTransactions(id: List<Long>): Single<TransactionApiResponse> {
        return doOnSuccess { apiResponse ->
            if (apiResponse is TransactionApiResponse.Success) {
                mTransactionRepository.deleteById(id)
            }
        }
    }

    // endregion Extra -----------------------------------------------------------------------------

}
