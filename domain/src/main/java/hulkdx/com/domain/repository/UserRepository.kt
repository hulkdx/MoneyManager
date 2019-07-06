package hulkdx.com.domain.repository

import hulkdx.com.domain.data.model.User

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/07/2019.
 */
interface UserRepository {

    fun getCurrentUser(): User?

    fun saveCurrentUser(user: User)

    fun deleteCurrentUser()

    fun updateCurrentUserAmount(amount: Float)

}