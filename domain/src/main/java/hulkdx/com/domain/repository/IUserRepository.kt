package hulkdx.com.domain.repository

import hulkdx.com.domain.models.User

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
interface IUserRepository {
    fun getUser(): User?
}