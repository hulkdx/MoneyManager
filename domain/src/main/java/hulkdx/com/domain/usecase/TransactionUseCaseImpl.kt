package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.remote.ApiManager
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
                .observeOn(mUiScheduler)
                .subscribe({ apiResponse ->
                    when (apiResponse) {
                        is ApiManager.TransactionApiResponse.Success -> {
                            val transactions = apiResponse.transactions
                            val amount = String.format("%.2f", apiResponse.totalAmount)
                            mTransactionRepository.save(transactions)
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

    override fun deleteTransactionsAsync(id: List<Long>, onComplete: (TransactionResult) -> Unit) {
//        val user = mDataSourceManager.getUser()
//        if (user == null) {
//            // Auth error!
//            onComplete(TransactionResult.AuthenticationError)
//            return
//        }
//        val disposable = mApiManager.deleteTransactions(user.token, id)
//                .subscribeOn(mBackgroundScheduler)
//                .observeOn(mUiScheduler)
//                .subscribe({
//                    TODO()
//                }, {
//
//                })
//
//        mDisposables.add(disposable)
    }

    override fun dispose() {
        mDisposables.clear()
    }

}
