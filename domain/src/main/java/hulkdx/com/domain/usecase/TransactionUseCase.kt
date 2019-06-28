package hulkdx.com.domain.usecase

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface TransactionUseCase {

    fun getTransactions(onComplete: (TransactionResult) -> (Unit))

    fun dispose()

    sealed class TransactionResult {

    }
}
