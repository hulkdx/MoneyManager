package hulkdx.com.domain.usecase

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface TransactionCategoryUseCase {

    fun getTransactionCategories(onComplete: (TransactionCategoryResult) -> (Unit))

    fun dispose()

    sealed class TransactionCategoryResult {

    }
}
