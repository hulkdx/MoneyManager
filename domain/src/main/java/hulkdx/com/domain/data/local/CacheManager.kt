package hulkdx.com.domain.data.local

import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.model.User

interface CacheManager {

    fun saveUser(user: User)
    fun getUser(): User?
    fun invalidateUser()
    fun updateUserAmount(amount: Float)

    fun saveTransactions(transactions: List<Transaction>)
    fun getTransactions(): List<Transaction>?
}