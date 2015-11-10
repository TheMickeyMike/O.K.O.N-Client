package com.roundstarstudio.maciej.okon.activities.api;


import com.roundstarstudio.maciej.okon.activities.api.model.*;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Maciej on 02.11.15.
 */
public interface OkonService {

    String ENDPOINT = "http://192.168.1.105:3000/api/v1/";
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/users/{username}")
    Call<User> getUser(@Path("username") String username);

    @GET("/statuses/statuses")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("/users/new")
    Call<User> createUser(@Body User user);


}
