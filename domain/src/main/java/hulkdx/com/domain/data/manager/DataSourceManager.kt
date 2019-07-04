package hulkdx.com.domain.data.manager

import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.model.User
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 *
 * Note: is there a better name for this class?
 */
interface DataSourceManager {

    /**
     * Get user from cache first if it doesn't exists get it from database and save it in cache
     */
    fun getUser(): User?

    fun getTransactions(): List<Transaction>
}