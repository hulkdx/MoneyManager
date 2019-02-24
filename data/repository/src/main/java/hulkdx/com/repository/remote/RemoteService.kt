//@file:Suppress("MemberVisibilityCanBePrivate")
//
//package hulkdx.com.repository.remote
//
//import com.google.gson.GsonBuilder
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//
///**
// * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
// */
//interface RemoteService {
//
//
//    object Factory {
//        const val ENDPOINT = "https://api.github.com/"
//
//        fun create(): RemoteService {
//            val client = OkHttpClient.Builder()
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .readTimeout(10, TimeUnit.SECONDS)
//                    .build()
//
//            val json = GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//                    .create()
//
//            val retrofit = Retrofit.Builder()
//                    .baseUrl(ENDPOINT)
//                    .client(client)
//                    .addConverterFactory(GsonConverterFactory.create(json))
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build()
//            return retrofit.create(RemoteService::class.java)
//        }
//    }
//}
