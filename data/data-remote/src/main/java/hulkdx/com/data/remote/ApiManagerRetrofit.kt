package hulkdx.com.data.remote

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


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


    @GET("transactions/get")
    fun getTransactions(@Header("Authorization") auth: String): Single<Response<ResponseBody>>

//    @FormUrlEncoded
//    @POST("transactions/create")
//    fun createTransaction(@Header("Authorization") auth: String,
//                          @Field("amount") amount: Float,
//                          @Field("date") date: String,
//                          @Field("attachment") attachment: String,
//                          @Field("category") categoryId: String): Single<Transaction>
//
//
//    @HTTP(method = "DELETE", path = "transactions/delete", hasBody = true)
//    fun deleteTransaction(@Header("Authorization") auth: String,
//                          @Body request: DeleteTransactionsRequestBody): Single<TransactionResponse>
//
//    @HTTP(method = "PUT", path = "transactions/update", hasBody = true)
//    fun updateTransaction(@Header("Authorization") auth: String,
//                          @Body request: UpdateTransactionRequest): Single<Any>
//
//    @GET("categories/get")
//    fun getCategories(@Header("Authorization") auth: String): Single<List<Category>>
//
//    @FormUrlEncoded
//    @POST("categories/create")
//    fun createCategory(@Header("Authorization") auth: String,
//                       @Field("name") name: String,
//                       @Field("hexColor") hexColor: String): Single<Category>
}