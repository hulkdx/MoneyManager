package hulkdx.com.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Singleton
class TransactionCategoryUseCaseImpl @Inject constructor(): TransactionCategoryUseCase {
    override fun getTransactionCategories(onComplete: (TransactionCategoryUseCase.TransactionCategoryResult) -> Unit) {
        // TODO
    }

    override fun dispose() {
        // TODO
    }
}
