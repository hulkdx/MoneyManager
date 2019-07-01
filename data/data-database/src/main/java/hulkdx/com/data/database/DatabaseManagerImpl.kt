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

    override fun saveUser(user: User) {
        user.run {
            return@run UserRealmObject(username, firstName, lastName, email, token, currency)
        }.execute { obj, realm ->
            realm.insertOrUpdate(obj)
        }
    }

    override fun getUser(): User? {
        var user: User? = null
        execute { _, realm ->
            val userObject = realm.where(UserRealmObject::class.java).findFirst()
            user = userObject?.map()
        }
        return user
    }

    override fun saveTransactions(transactions: List<Transaction>) {
        transactions.map {
            val category = it.category?.run {
                return@run CategoryRealmObject(id, name, hexColor)
            }

            return@map TransactionRealmObject(it.id, it.date, category, it.amount, it.attachment)
        }.execute { listTransactionRealmObject, realm ->
            realm.copyToRealmOrUpdate(listTransactionRealmObject)
        }
    }

    private fun getRealm(): Realm {
        return Realm.getInstance(mRealmConfiguration)
    }

    private inline fun <T> T.execute(execution: (T, Realm) -> (Unit)) {
        var realm: Realm? = null
        try {
            mLock.lock()
            realm = getRealm()
            realm.beginTransaction()
            execution(this, realm)
            realm.commitTransaction()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            realm?.close()
            mLock.unlock()
        }
    }
}
