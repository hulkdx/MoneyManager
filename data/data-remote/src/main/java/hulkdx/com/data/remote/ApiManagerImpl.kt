package hulkdx.com.data.remote

import com.google.gson.JsonParser
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.ApiManager.*
import hulkdx.com.domain.data.remote.RemoteStatus
import io.reactivex.Single
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class ApiManagerImpl @Inject constructor(
        private val mApiManagerRetrofit: ApiManagerRetrofit
): ApiManager {

    override fun loginSync(username: String, password: String): Single<LoginApiResponse> {
        return mApiManagerRetrofit.postLogin(username, password).map { it ->
            when (it.code()) {
                200 -> {
                    val jsonString = it.body()!!.string()
                    var resultJsonObject = JsonParser().parse(jsonString).asJsonObject

                    var username  = ""
                    var firstName = ""
                    var lastName  = ""
                    var email     = ""
                    var currency  = ""
                    var token     = ""
                    
                    for ((key, value) in resultJsonObject.entrySet()) {
                        when (key) {
                            "username" -> {
                                username = value.asString
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

                    return@map LoginApiResponse(RemoteStatus.SUCCESS, username, firstName, lastName,
                            email, currency, token)
                }
                500 -> {
                    // invalid username and password
                    it.errorBody()?.string()?.let { jsonErrorString ->
                        JsonParser().parse(jsonErrorString).asJsonObject.get("error").asString?.apply {
                            return@map LoginApiResponse(RemoteStatus.AUTH_ERROR, "",
                                    "", "", "", "", "")
                        }
                    }
                }
            }

            return@map LoginApiResponse(RemoteStatus.GENERAL_ERROR, "",
                    "", "", "", "", "")
        }
    }

}
