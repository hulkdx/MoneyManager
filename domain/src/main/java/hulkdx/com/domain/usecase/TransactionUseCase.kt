package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.model.Transaction

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface TransactionUseCase {

    fun getTransactionsAsync(onComplete: (GetTransactionResult) -> (Unit))
    fun deleteTransactionsAsync(positions: Set<Int>,
                                id: List<Long>,
                                onComplete: (DeleteTransactionResult) -> (Unit))
    fun searchTransactionsAsync(searchText: String, onComplete: (List<Transaction>) -> (Unit))

    fun dispose()

    sealed class GetTransactionResult {
        object AuthenticationError : GetTransactionResult()
        class Success(val transactions: List<Transaction>,
                      val amount: String,
                      val currencyName: String) : GetTransactionResult()
        class NetworkError(val throwable: Throwable): GetTransactionResult()
        class GeneralError(val throwable: Throwable? = null): GetTransactionResult()
    }

    sealed class DeleteTransactionResult {
        class Success(
                val amount: String,
                val newTransactions: List<Transaction>
        ) : DeleteTransactionResult()
        class AuthenticationError(
                val oldTransactions: List<Transaction>
        ): DeleteTransactionResult()
        class NetworkError(
                val oldTransactions: List<Transaction>,
                val throwable: Throwable
        ): DeleteTransactionResult()
        class GeneralError(
                val oldTransactions: List<Transaction>,
                val throwable: Throwable? = null
        ): DeleteTransactionResult()
        class UnknownError(val throwable: Throwable): DeleteTransactionResult()
    }
}
