package hulkdx.com.data.remote

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded



/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface ApiManagerRetrofit {

    @FormUrlEncoded
    @POST("login")
    fun postLogin(@Field("username") username: String,
                  @Field("password") password: String): Single<Response<ResponseBody>>
}