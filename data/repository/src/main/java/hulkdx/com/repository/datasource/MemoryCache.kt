package hulkdx.com.repository.datasource

import hulkdx.com.domain.models.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@Singleton
class MemoryCache @Inject constructor(): IMemoryCache {

    private var mUser: User? = null

    override fun setUser(user: User) {
        synchronized(this) {
            mUser = user
        }
    }

    override fun getUser(): User? {
        synchronized(this) {
            return mUser
        }
    }

}