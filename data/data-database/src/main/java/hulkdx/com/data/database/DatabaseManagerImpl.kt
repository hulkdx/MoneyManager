package hulkdx.com.data.database

import hulkdx.com.data.model.UserRealmObject
import hulkdx.com.domain.data.database.DatabaseManager
import hulkdx.com.domain.data.model.User
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject



/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class DatabaseManagerImpl @Inject constructor(
        private val mRealmConfiguration: RealmConfiguration
): DatabaseManager {

    private val mLock = ReentrantLock()

    override fun saveUser(user: User) {
        user.run {
            val userRealmObject = UserRealmObject(username, firstName, lastName, email, token,
                    currency)
            return@run userRealmObject
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
