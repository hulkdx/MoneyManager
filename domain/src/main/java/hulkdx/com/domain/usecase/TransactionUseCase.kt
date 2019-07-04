package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.model.Transaction

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface TransactionUseCase {

    fun getTransactionsAsync(onComplete: (TransactionResult) -> (Unit))
    fun searchTransactionsAsync(searchText: String, onComplete: (List<Transaction>) -> (Unit))

    fun dispose()

    sealed class TransactionResult {
        object AuthenticationError : TransactionResult()
        class Success(val transactions: List<Transaction>, val amount: String, val currencyName: String) : TransactionResult()
        class NetworkError(val throwable: Throwable): TransactionResult()
        class GeneralError(val throwable: Throwable? = null): TransactionResult()
    }
}
