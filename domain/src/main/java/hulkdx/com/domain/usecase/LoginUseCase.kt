package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.remote.RemoteStatus

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface LoginUseCase {

    fun loginAsync(username: String, password: String, onComplete: (LoginResult) -> (Unit))
    fun dispose()

    data class LoginResult(
            val status: RemoteStatus,
            var throwable: Throwable? = null
    )
}
