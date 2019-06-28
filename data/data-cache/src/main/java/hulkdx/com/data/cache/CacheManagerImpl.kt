package hulkdx.com.data.cache

import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.model.User
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject

/**
 *  Cache the data that is needed into memory.
 */
class CacheManagerImpl @Inject constructor(): CacheManager {

    private var mLock = ReentrantLock()
    private var mUser: User? = null
        get() {
            try {
                mLock.lock()
                return field
            } finally {
                mLock.unlock()
            }
        }
        set(value) {
            try {
                mLock.lock()
                field = value
            } finally {
                mLock.unlock()
            }
        }

    override fun saveUser(user: User) {
        mUser = user
    }

    override fun getUser(): User? {
        return mUser
    }

    override fun invalidateUser() {
        mUser = null
    }
}
