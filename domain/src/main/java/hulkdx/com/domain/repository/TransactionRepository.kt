package hulkdx.com.domain.repository

import hulkdx.com.domain.data.model.Transaction

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/07/2019.
 */
interface TransactionRepository {

    fun findAll(): List<Transaction>
    fun findByAbsoluteAmount(amount: Float): List<Transaction>
    fun findByCategoryName(categoryName: String): List<Transaction>

    fun save(transactions: List<Transaction>)

}