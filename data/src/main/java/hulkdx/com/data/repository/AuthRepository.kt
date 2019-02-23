package hulkdx.com.data.repository

import hulkdx.com.domain.repository.IAuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@Singleton
class AuthRepository @Inject constructor(): IAuthRepository {

    override fun isLoggedIn(): Boolean {
        return false
    }

}