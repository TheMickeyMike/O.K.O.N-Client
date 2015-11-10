package com.roundstarstudio.maciej.okon.activities.api.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.BuildConfig;
import com.roundstarstudio.maciej.okon.activities.api.OkonAuthService;


import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class Connections {

    private static final String AUTH_ENDPOINT = "http://192.168.1.105:3000";

    public void run(String email, String password) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AUTH_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OkonAuthService okonAuthService = retrofit.create(OkonAuthService.class);

        Call<AccessToken> call = okonAuthService.getAccessToken(BuildConfig.CLIENT_KEY,BuildConfig.CLIENT_SECRET,"password",email,password);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Response<AccessToken> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    int statusCode = response.code();
                    AccessToken accessToken = response.body();
                    System.out.println(statusCode);
                    System.out.println(accessToken.getAccess_token() + "   " + accessToken.getToken_type() + "  " );
                    System.out.println(response.body().toString());
                } else {
                    System.out.println("HIUSTON MAMAY PROBLEM");
                    //TODO catch code error
                }
            }
            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


//
//    private final OkHttpClient client = new OkHttpClient();
//
//    public void run(String email, String password) throws Exception {
//
//        final AccessToken[] accessToken = {null};
//
//        Request request = new Request.Builder()
//                .url(AUTH_ENDPOINT)
//                .header("Accept", "application/json")
//                .post(new FormEncodingBuilder()
//                        .add("client_id", CLIENT_ID)
//                        .add("client_secret", CLIENT_SECRET)
//                        .add("grant_type", "password")
//                        .add("password", password)
//                        .add("username", email)
//                        .build())
//                .build();
//
//        // Get a handler that can be used to post to the main thread
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(final Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                }
//
//                Headers responseHeaders = response.headers();
//                for (int i = 0; i < responseHeaders.size(); i++) {
//                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }
//                accessToken[0] = parseJson(response.body().string()); // Konwersja do Gson
//                System.out.println(response.body().string());
//            }
//        });
//    }




}
