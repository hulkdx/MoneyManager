package hulkdx.com.domain.data.local

import hulkdx.com.domain.data.model.User

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface DatabaseManager {
    fun getUser(): User?
    fun saveUser(user: User)
}