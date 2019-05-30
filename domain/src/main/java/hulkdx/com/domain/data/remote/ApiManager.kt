package hulkdx.com.domain.data.remote

import io.reactivex.Single

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface ApiManager {
    fun loginSync(username: String, password: String): Single<LoginApiResponse>

    data class LoginApiResponse(val status: RemoteStatus, val token: String)
}