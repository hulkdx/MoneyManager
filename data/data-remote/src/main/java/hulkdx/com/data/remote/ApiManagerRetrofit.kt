package hulkdx.com.data.remote

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded



/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 *
 *
 * TODO: remove FormUrlEncoded in the server.
 * TODO: change register API => 1. add first name, last name. 2. remove email2
 * TODO: add some authentication for register API.
 * TODO: register API doesn't return a token. make it return a token.
 */
interface ApiManagerRetrofit {

    @FormUrlEncoded
    @POST("login")
    fun postLogin(@Field("username") username: String,
                  @Field("password") password: String): Single<Response<ResponseBody>>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(@Field("username") username: String,
                     @Field("password") password: String,
                     @Field("email") email: String,
                     @Field("email2") email2: String,
                     @Field("currency") currency: String): Single<Response<ResponseBody>>
}