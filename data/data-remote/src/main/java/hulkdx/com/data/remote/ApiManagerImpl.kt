package hulkdx.com.data.remote

import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.ApiManager.*
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class ApiManagerImpl @Inject constructor(): ApiManager {
    override fun loginSync(username: String, password: String): Single<LoginApiResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}