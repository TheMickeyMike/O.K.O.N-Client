package com.roundstarstudio.maciej.okon.activities.api;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Maciej on 10.11.15.
 */
public class AuthInterceptor {

    private final String accessToken;

    public AuthInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient();

        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + accessToken).build();
                return chain.proceed(newRequest);
            }
        };

        // Add the interceptor to OkHttpClient
        client.interceptors().add(interceptor);

        return client;
    }
}
