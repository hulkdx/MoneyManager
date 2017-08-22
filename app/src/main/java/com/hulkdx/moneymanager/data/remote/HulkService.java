package com.hulkdx.moneymanager.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hulkdx.moneymanager.data.model.User;
import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/18/2017.
 */

public interface HulkService {
    String ENDPOINT = "https://moneymanagerv2.herokuapp.com/api/";

    @FormUrlEncoded
    @POST("login")
    Flowable<User> postLogin(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Flowable<User> postRegister(@Field("username") String username,
                                @Field("password") String password,
                                @Field("email") String email,
                                @Field("email2") String email2);

    class Creator {
        public static HulkService newService() {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(HulkService.class);
        }
    }
}
