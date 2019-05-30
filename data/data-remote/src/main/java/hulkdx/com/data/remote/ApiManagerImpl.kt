package hulkdx.com.data.remote

import com.google.gson.JsonParser
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.ApiManager.*
import hulkdx.com.domain.data.remote.RemoteStatus
import io.reactivex.Single
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Singleton
class ApiManagerImpl @Inject constructor(
        private val mApiManagerRetrofit: ApiManagerRetrofit
): ApiManager {

    override fun loginSync(username: String, password: String): Single<LoginApiResponse> {
        return mApiManagerRetrofit.postLogin(username, password).map {
            when (it.code()) {
                200 -> {
                    val jsonString = it.body()!!.string()
                    var resultJsonObject = JsonParser().parse(jsonString).asJsonObject

                    var usernameJ = ""
                    var firstName = ""
                    var lastName  = ""
                    var email     = ""
                    var currency  = ""
                    var token     = ""
                    
                    for ((key, value) in resultJsonObject.entrySet()) {
                        when (key) {
                            "username" -> {
                                usernameJ = value.asString
                            }
                            "first_name" -> {
                                firstName = value.asString
                            }
                            "last_name" -> {
                                lastName = value.asString
                            }
                            "email" -> {
                                email = value.asString
                            }
                            "token" -> {
                                token = value.asString
                            }
                            "currency" -> {
                                currency = value.asString
                            }
                        }
                    }

                    val user = User(usernameJ, firstName, lastName,
                            email, currency, token)
                    return@map LoginApiResponse(RemoteStatus.SUCCESS, user)
                }
                500 -> {
                    // invalid username and password
                    it.errorBody()?.string()?.let { jsonErrorString ->
                        JsonParser().parse(jsonErrorString).asJsonObject.get("error").asString?.apply {
                            val user = User("", "", "", "", "", "")
                            return@map LoginApiResponse(RemoteStatus.AUTH_ERROR, user)
                        }
                    }
                }
            }

            return@map LoginApiResponse(RemoteStatus.GENERAL_ERROR, User("",
                    "", "", "", "", ""))
        }
    }

}
