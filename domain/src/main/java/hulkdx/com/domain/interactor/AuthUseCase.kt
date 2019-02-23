package hulkdx.com.domain.interactor

import hulkdx.com.domain.executor.PostExecutionThread
import hulkdx.com.domain.repository.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@Singleton
class AuthUseCase @Inject constructor(private val mUserRepository: IUserRepository) {

    fun isLoggedIn(): Boolean {
        val user = mUserRepository.getUser()
        return user != null
    }

}