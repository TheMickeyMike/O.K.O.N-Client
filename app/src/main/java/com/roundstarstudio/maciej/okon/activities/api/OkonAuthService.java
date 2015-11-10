package com.roundstarstudio.maciej.okon.activities.api;

import com.roundstarstudio.maciej.okon.activities.api.model.AccessToken;
import com.roundstarstudio.maciej.okon.activities.api.model.User;

import java.util.List;

import retrofit.Call;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface OkonAuthService {

    public static final String ENDPOINT = "http://192.168.1.105:3000";

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/users/{username}")
    Call<User> getUser(@Path("username") String username);

    @GET("/statuses/statuses")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("/users/new")
    Call<User> createUser(@Body User user);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getAccessToken(@Field("client_id") String client_id,
                               @Field("client_secret") String client_secret,
                               @Field("grant_type") String grant_type,
                               @Field("username") String username,
                               @Field("password") String password);


}
