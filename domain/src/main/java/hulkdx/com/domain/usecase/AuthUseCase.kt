package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.remote.RegisterAuthError
import hulkdx.com.domain.data.remote.RemoteStatus

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface AuthUseCase {

    fun loginAsync(username: String, password: String, onComplete: (LoginResult) -> (Unit))
    fun registerAsync(firstName: String,
                      lastName: String,
                      username: String,
                      password: String,
                      email: String,
                      currency: String,
                      onComplete: (RegisterResult) -> (Unit))
    fun dispose()

    data class LoginResult (
            val status: RemoteStatus,
            var throwable: Throwable? = null
    )

    sealed class RegisterResult {
        object Successful: RegisterResult()
        object AuthError: RegisterResult()
        data class NetworkError(val throwable: Throwable? = null): RegisterResult()
        data class GeneralError(val throwable: Throwable? = null): RegisterResult()
    }
}
