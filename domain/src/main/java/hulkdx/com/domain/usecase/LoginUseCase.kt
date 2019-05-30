package hulkdx.com.domain.usecase

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface LoginUseCase {

    fun loginAsync(username: String, password: String, onComplete: (LoginResult) -> (Unit))

    data class LoginResult(val status: Int)

    // region LoginResult Status
    companion object {
        const val LOGIN_RESULT_SUCCESS = 1
    }
}
