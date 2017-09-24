package com.hulkdx.moneymanager.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.data.model.TransactionResponse;
import com.hulkdx.moneymanager.data.model.User;
import java.util.List;
import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/18/2017.
 */

public interface HulkService {
    String ENDPOINT = "https://moneymanagerv2.herokuapp.com/api/";

    @FormUrlEncoded
    @POST("login")
    Flowable<User> postLogin(@Field("username") String username,
                             @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Flowable<User> postRegister(@Field("username") String username,
                                @Field("password") String password,
                                @Field("email") String email,
                                @Field("email2") String email2,
                                @Field("currency") String currency);

    @GET("transactions/get")
    Flowable<TransactionResponse> getTransactions(@Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("transactions/create")
    Flowable<Transaction> createTransaction(@Header("Authorization") String auth,
                                            @Field("amount") float amount,
                                            @Field("date") String date,
                                            @Field("attachment") String attachment,
                                            @Field("category") String categoryId);

    @GET("categories/get")
    Flowable<List<Category>> getCategories(@Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("categories/create")
    Flowable<Category> createCategory(@Header("Authorization") String auth,
                                      @Field("name") String name,
                                      @Field("hexColor") String hexColor);

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
