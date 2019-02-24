package hulkdx.com.repository.datasource

import hulkdx.com.domain.models.User

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
interface IUserDataSource {
    fun getUser(): User?
    fun setUser(user: User)
}