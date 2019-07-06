package hulkdx.com.domain.repository

import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.model.User
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/07/2019.
 */
class UserRepositoryImpl @Inject constructor(
        private val mDatabaseManager: DatabaseManager,
        private val mCacheManager: CacheManager
): UserRepository {

    override fun getCurrentUser(): User? {
        var user = mCacheManager.getUser()
        if (user == null) {
            user = mDatabaseManager.getUser()
            if (user != null) {
                mCacheManager.saveUser(user)
            }
        }
        return user
    }

    override fun saveCurrentUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteCurrentUser() {
        mCacheManager.invalidateUser()
        mDatabaseManager.deleteUser()
    }

}
