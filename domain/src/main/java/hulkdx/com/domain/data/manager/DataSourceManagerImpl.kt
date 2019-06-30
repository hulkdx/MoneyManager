package hulkdx.com.domain.data.manager

import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.model.User
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 */
class DataSourceManagerImpl @Inject constructor(
        private val mDatabaseManager: DatabaseManager,
        private val mCacheManager: CacheManager
): DataSourceManager {

    override fun getUser(): User? {
        var user = mCacheManager.getUser()
        if (user == null) {
            user = mDatabaseManager.getUser()
            if (user != null) {
                mCacheManager.saveUser(user)
            }
        }
        return user
    }
}