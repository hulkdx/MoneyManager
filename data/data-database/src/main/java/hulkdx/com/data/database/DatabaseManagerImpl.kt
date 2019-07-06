package hulkdx.com.data.database

import hulkdx.com.data.database.model.CategoryRealmObject
import hulkdx.com.data.database.model.TransactionRealmObject
import hulkdx.com.data.database.model.UserRealmObject
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.model.User
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Singleton
class DatabaseManagerImpl @Inject constructor(
        private val mRealmConfiguration: RealmConfiguration
): DatabaseManager {

    private val mLock = ReentrantLock()

    // region User ---------------------------------------------------------------------------------

    override fun saveUser(user: User) {
        user.run {
            return@run UserRealmObject(username, firstName, lastName, email, token, currency)
        }.execute { obj, realm ->
            realm.beginTransaction()
            realm.insertOrUpdate(obj)
            realm.commitTransaction()
        }
    }

    override fun getUser(): User? {
        var user: User? = null
        execute { _, realm ->
            val userObject = realm.where(UserRealmObject::class.java).findFirst()
            user = userObject?.mapToUser()
        }
        return user
    }

    override fun deleteUser() {
        execute { _, realm ->
            val allUsers = realm.where(UserRealmObject::class.java).findAll()
            realm.beginTransaction()
            allUsers.deleteAllFromRealm()
            realm.commitTransaction()
        }
    }

    override fun updateUserAmount(amount: Float) {
        execute { _, realm ->
            val user = realm.where(UserRealmObject::class.java).findFirst()
            realm.beginTransaction()
            user?.amount = amount
            realm.commitTransaction()
        }
    }

    // endregion User ------------------------------------------------------------------------------
    // region Transaction --------------------------------------------------------------------------

    override fun saveTransactions(transactions: List<Transaction>) {
        transactions.map {
            val category = it.category?.run {
                return@run CategoryRealmObject(id, name, hexColor)
            }

            return@map TransactionRealmObject(it.id, it.date, category, it.amount, it.attachment)
        }.execute { listTransactionRealmObject, realm ->
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(listTransactionRealmObject)
            realm.commitTransaction()
        }
    }

    override fun getTransactions(): List<Transaction>? {
        var result: List<Transaction>? = null

        execute { _, realm ->
            val transactionRealmObject = realm.where(TransactionRealmObject::class.java).findAll()
            result = transactionRealmObject.map {
                return@map it.mapToTransaction()
            }
        }

        return result
    }

    // endregion Transaction ----------------------------------------------------------------------
    // region Extra --------------------------------------------------------------------------------

    private fun getRealm(): Realm {
        return Realm.getInstance(mRealmConfiguration)
    }

    private inline fun <T> T.execute(execution: (T, Realm) -> (Unit)) {
        var realm: Realm? = null
        try {
            mLock.lock()
            realm = getRealm()
            execution(this, realm)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            realm?.close()
            mLock.unlock()
        }
    }

    // endregion Extra -----------------------------------------------------------------------------

}
