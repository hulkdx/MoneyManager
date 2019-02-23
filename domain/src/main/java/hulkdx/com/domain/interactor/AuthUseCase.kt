package hulkdx.com.domain.interactor

import hulkdx.com.domain.repository.IAuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@Singleton
class AuthUseCase @Inject constructor(val mAuthRepository: IAuthRepository) {

    fun isLoggedIn(): Boolean {
        return mAuthRepository.isLoggedIn()
    }

}