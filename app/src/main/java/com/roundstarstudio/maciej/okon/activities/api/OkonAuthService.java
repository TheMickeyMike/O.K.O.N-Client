package com.roundstarstudio.maciej.okon.activities.api;

import com.roundstarstudio.maciej.okon.activities.api.model.AccessToken;

import retrofit.Call;


import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface OkonAuthService {

    public static final String ENDPOINT = "http://coputer.science:3000";


    //TODO Revokes the given token, requires authentication (logout?)
    @FormUrlEncoded
    @POST("/oauth/revoke")
    Call<AccessToken> createUser(@Field("token") String access_token);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getAccessToken(@Field("client_id") String client_id,
                               @Field("client_secret") String client_secret,
                               @Field("grant_type") String grant_type,
                               @Field("username") String username,
                               @Field("password") String password);


}
