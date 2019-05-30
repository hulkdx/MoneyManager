package hulkdx.com.data.remote.di

import dagger.Module
import dagger.Provides
import hulkdx.com.data.remote.ApiManagerImpl
import hulkdx.com.data.remote.ApiManagerRetrofit
import hulkdx.com.domain.data.remote.ApiManager
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.GsonBuilder
import hulkdx.com.domain.util.API_BASE_URL
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Module
object NetworkModule {
    @JvmStatic
    @Provides
    fun provideApiManager(apiManagerImpl: ApiManagerImpl): ApiManager = apiManagerImpl

    @JvmStatic
    @Provides
    fun provideApiManagerRetrofit(): ApiManagerRetrofit {
        val client = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build()
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(ApiManagerRetrofit::class.java)
    }
}
