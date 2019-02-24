package hulkdx.com.repository.repository

import hulkdx.com.repository.datasource.IDataBase
import hulkdx.com.domain.models.User
import hulkdx.com.domain.repository.IUserRepository
import hulkdx.com.repository.datasource.IMemoryCache
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@Singleton
class UserRepository @Inject constructor(private val mDataBase: IDataBase,
                                         private val mMemoryCache: IMemoryCache): IUserRepository {

    override fun getUser(): User? {

        val cachedUser = mMemoryCache.getUser()
        if (cachedUser != null) return cachedUser

        val user = mDataBase.getUser()
        user?.let {
            mMemoryCache.setUser(user)
        }

        return user
    }
}
