package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.ApiManager.*
import hulkdx.com.domain.di.BackgroundScheduler
import hulkdx.com.domain.di.UiScheduler
import hulkdx.com.domain.repository.TransactionRepository
import hulkdx.com.domain.repository.UserRepository
import hulkdx.com.domain.usecase.TransactionUseCase.*
import hulkdx.com.domain.util.UserNotExistsException
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 *
 * TODO: deleteTransactionsAsync == deleting from the api succeed and deleting from db is not.
 *
 * TODO: Add a mapper class for mapping the amount.
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

    override fun getTransactionsAsync(onComplete: (GetTransactionResult) -> Unit)
    {
        val disposable= Single.fromCallable { getTransaction() }
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mUiScheduler)
                .subscribe({ pair ->
                    val apiResponse = pair.first
                    val user = pair.second

                    when (apiResponse) {
                        is TransactionApiResponse.Success<GetTransactionApiResponse> -> {
                            val (transactions, totalAmount) = apiResponse.data
                            val amount = formatAmount(totalAmount)
                            onComplete(
                                    GetTransactionResult.Success(
                                            transactions,
                                            amount,
                                            user.currency
                                    )
                            )
                        }
                        is TransactionApiResponse.GeneralError -> {
                            onComplete(GetTransactionResult.GeneralError())
                        }
                        is TransactionApiResponse.AuthWrongToken -> {
                            onComplete(GetTransactionResult.AuthenticationError)
                        }
                    }
                }, { throwable ->
                    when (throwable) {
                        is IOException -> onComplete(GetTransactionResult.NetworkError(throwable))
                        is UserNotExistsException -> { onComplete(GetTransactionResult.AuthenticationError) }
                        else -> onComplete(GetTransactionResult.GeneralError(throwable))
                    }
                })

        mDisposables.add(disposable)
    }

    private fun getTransaction(): Pair<TransactionApiResponse<GetTransactionApiResponse>, User> {
        val user = mUserRepository.getCurrentUser() ?: throw UserNotExistsException()

        val apiResponse = mApiManager.getTransactions(user.token)
        if (apiResponse is TransactionApiResponse.Success) {
            mTransactionRepository.save(apiResponse.data.transactions)
            mUserRepository.updateCurrentUserAmount(apiResponse.data.totalAmount)
        }
        return Pair(apiResponse, user)
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

    override fun deleteTransactionsAsync(positions: Set<Int>,
                                         id: List<Long>,
                                         onComplete: (DeleteTransactionResult) -> Unit)
    {

        val disposable = deleteTransactions(positions, id)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mUiScheduler)
                .subscribe({ deleteTransactionResult ->
                    onComplete(deleteTransactionResult)
                }, {
                    // Should be avoided...
                    onComplete(DeleteTransactionResult.UnknownError(it))
                })

        mDisposables.add(disposable)
    }

    /**
     * positions is a sorted set.
     */
    private fun deleteTransactions(positions: Set<Int>, id: List<Long>): Flowable<DeleteTransactionResult>
    {
        return Flowable.create({ emitter ->
            val oldTransactions = mTransactionRepository.findAll()

            try {
                val newTransactions: MutableList<Transaction> = ArrayList(oldTransactions)

                val user = mUserRepository.getCurrentUser() ?: throw UserNotExistsException()

                var amount = user.amount
                for (position in positions.reversed()) {
                    val transaction = newTransactions.removeAt(position)
                    amount -= transaction.amount
                }

                //
                // Immediately run success and try to revert back if a problem occurs...
                //
                emitter.onNext(
                        DeleteTransactionResult.Success(
                                formatAmount(amount),
                                newTransactions
                        )
                )

                when (mApiManager.deleteTransactions(user.token, id)) {
                    is TransactionApiResponse.Success<DeleteTransactionApiResponse> -> {
                        mTransactionRepository.save(newTransactions)
                    }
                    is TransactionApiResponse.GeneralError -> {
                        emitter.onNext(DeleteTransactionResult.GeneralError(oldTransactions))
                    }
                    is TransactionApiResponse.AuthWrongToken -> {
                        emitter.onNext(DeleteTransactionResult.AuthenticationError(oldTransactions))
                    }
                }

            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> emitter.onNext(DeleteTransactionResult.NetworkError(oldTransactions, throwable))
                    is UserNotExistsException -> { emitter.onNext(DeleteTransactionResult.AuthenticationError(oldTransactions)) }
                    else -> emitter.onNext(DeleteTransactionResult.GeneralError(oldTransactions))
                }
            }
        }, BackpressureStrategy.LATEST)

    }

    // region Extra --------------------------------------------------------------------------------

    override fun dispose() {
        mDisposables.clear()
    }

    private fun formatAmount(amount: Float): String = String.format("%.2f", amount)

    // endregion Extra -----------------------------------------------------------------------------

}
