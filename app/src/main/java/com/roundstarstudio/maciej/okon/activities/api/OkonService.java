package com.roundstarstudio.maciej.okon.activities.api;


import com.roundstarstudio.maciej.okon.activities.api.model.*;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Maciej on 02.11.15.
 */
public interface OkonService {

   public static final String ENDPOINT = "http://192.168.1.105:3000/api/v1/";

    /*Statuses*/

    @GET("statuses/statuses")
    Call<List<Status>> getAllStatuses();

    @GET("statuses/feed")
    Call<List<Status>> getFeed(@Query("count") Integer count,
                               @Query("min_id") Integer min_id,
                               @Query("max_id") Integer max_id);

    @GET("statuses/{userid}")
    Call<List<Status>> getUserFeed(@Path("userid") String user_id);

    @POST("statuses/new")
    Call<NewStatus> createUser(@Body NewStatus status);

    @PUT("statuses/{id}")
    Call<Status> updateStatus(@Path("id") String status_id);

    @DELETE("statuses/{id}")
    Call<Status> deleteStatus(@Path("id") String status_id);



    /*Users*/

    @GET("users/me")
    Call<User> getMe();

    @GET("users/id/{id}")
    Call<User> getUser(@Path("id") int id);

    @GET("users/recent/{id}")
    Call<List<Status>> getPrivateFeed(@Path("id") int id,
                                      //@Query("id") int id,
                                      @Query("count") Integer count,
                                      @Query("min_id") Integer min_id,
                                      @Query("max_id") Integer max_id,
                                      @Query("min_timestamp") String min_timestamp, //TODO Date Format ("yyyy-MM-dd'T'HH:mm:ssZ")
                                      @Query("max_timestamp") String max_timestamp);    //TODO Date Format ("yyyy-MM-dd'T'HH:mm:ssZ")

    @POST("users/sign_up")
    Call<NewUser> createUser(@Query("first_name") String first_name,
                          @Query("last_name") String last_name,
                          @Query("profile_name") String profile_name,
                          @Query("email") String email,
                          @Query("password") String password,
                          @Query("password_confirmation") String password_confirmation);

    @POST("users/sign_up")
    Call<NewUser> signUp(@Body NewUser user);



}
