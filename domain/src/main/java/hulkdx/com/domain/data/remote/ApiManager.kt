package hulkdx.com.domain.data.remote

import hulkdx.com.domain.data.model.User
import io.reactivex.Single

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface ApiManager {
    fun loginSync(username: String, password: String): Single<LoginApiResponse>

    data class LoginApiResponse(
            val status: RemoteStatus,
            val user: User,
            val token: String
    )
}