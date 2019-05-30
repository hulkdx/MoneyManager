package hulkdx.com.domain.usecase

import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class LoginUseCaseImpl @Inject constructor(): LoginUseCase {

    override fun loginAsync(username: String,
                            password: String,
                            onComplete: (LoginUseCase.LoginResult) -> Unit) {
    }

}
