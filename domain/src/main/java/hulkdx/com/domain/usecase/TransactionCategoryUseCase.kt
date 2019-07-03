package hulkdx.com.domain.usecase

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface TransactionCategoryUseCase {

    fun getTransactionCategories(onComplete: (TransactionCategoryResult) -> (Unit))

    fun dispose()

    sealed class TransactionCategoryResult {
        class Success(): TransactionCategoryResult()
        object Loading: TransactionCategoryResult()
        object AuthenticationError : TransactionCategoryResult()
        class NetworkError(val throwable: Throwable): TransactionCategoryResult()
        class GeneralError(val throwable: Throwable? = null): TransactionCategoryResult()
    }
}
