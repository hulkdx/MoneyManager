package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.model.Transaction

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface TransactionUseCase {

    fun getTransactionsAsync(onComplete: (TransactionResult<GetTransactionResult>) -> (Unit))
    fun deleteTransactionsAsync(id: List<Long>, onComplete: (TransactionResult<DeleteTransactionResult>) -> (Unit))
    fun searchTransactionsAsync(searchText: String, onComplete: (List<Transaction>) -> (Unit))

    fun dispose()

    sealed class TransactionResult<out T> {
        object AuthenticationError : TransactionResult<Nothing>()
        class Success<T>(val data: T) : TransactionResult<T>()
        class NetworkError(val throwable: Throwable): TransactionResult<Nothing>()
        class GeneralError(val throwable: Throwable? = null): TransactionResult<Nothing>()
    }

    data class GetTransactionResult (
        val transactions: List<Transaction>, val amount: String, val currencyName: String
    )

    data class DeleteTransactionResult (
            val id: List<Long>, val amount: String
    )
}
