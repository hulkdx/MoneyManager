package hulkdx.com.domain.repository

import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.model.Transaction
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/07/2019.
 */
class TransactionRepositoryImpl @Inject constructor(
        private val mDatabaseManager: DatabaseManager,
        private val mCacheManager: CacheManager
): TransactionRepository {

    override fun findAll(): List<Transaction> {
        var transactions = mCacheManager.getTransactions()
        if (transactions == null) {
            transactions = mDatabaseManager.getTransactions()
            if (transactions != null) {
                mCacheManager.saveTransactions(transactions)
            }
        }
        return transactions ?: emptyList()
    }

    override fun findByAbsoluteAmount(amount: Float): List<Transaction> {
        val transactions = findAll()
        return transactions.filter {
            return@filter abs(amount) == abs(it.amount)
        }
    }

    override fun findByCategoryName(categoryName: String): List<Transaction> {
        val transactions = findAll()
        return transactions.filter {
            return@filter categoryName == it.category?.name
        }
    }

    override fun save(transactions: List<Transaction>) {
        mCacheManager.saveTransactions(transactions)
        mDatabaseManager.saveTransactions(transactions)
    }

    override fun deleteById(id: List<Long>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}