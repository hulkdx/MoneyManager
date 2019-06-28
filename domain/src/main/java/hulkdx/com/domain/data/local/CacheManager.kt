package hulkdx.com.domain.data.local

import hulkdx.com.domain.data.model.User

interface CacheManager {
    fun saveUser(user: User)
    fun getUser(): User?
    fun invalidateUser()
}