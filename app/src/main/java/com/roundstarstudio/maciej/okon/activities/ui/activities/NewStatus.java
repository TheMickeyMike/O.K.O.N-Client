package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.AuthInterceptor;
import com.roundstarstudio.maciej.okon.activities.api.OkonService;
import com.roundstarstudio.maciej.okon.activities.api.model.NewUser;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class NewStatus extends AppCompatActivity {

    private Button createBt;
    private EditText content;

    private UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Init LocalStore, we need to pass Context
        userLocalStore = new UserLocalStore(this);



        content = (EditText) findViewById(R.id.contentEt);
        createBt = (Button) findViewById(R.id.createbt);

        createBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.roundstarstudio.maciej.okon.activities.api.model.NewStatus status =
                        new com.roundstarstudio.maciej.okon.activities.api.model.NewStatus(content.getText().toString());
                sendToServer(status);
            }
        });




    }

    private void sendToServer(com.roundstarstudio.maciej.okon.activities.api.model.NewStatus user) {

        OkHttpClient authClient = new AuthInterceptor(userLocalStore
                .getAccessToken())
                .getOkHttpClient();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OkonService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(authClient)
                .build();

        OkonService apiService =
                retrofit.create(OkonService.class);

        Call<com.roundstarstudio.maciej.okon.activities.api.model.NewStatus> call = apiService.createUser(user);

        call.enqueue(new Callback<com.roundstarstudio.maciej.okon.activities.api.model.NewStatus>() {
            @Override
            public void onResponse(Response<com.roundstarstudio.maciej.okon.activities.api.model.NewStatus> response,
                                   Retrofit retrofit) {

                if (response.isSuccess()) {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    Toast.makeText(NewStatus.this,
                            "Utworzono!", Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println("HIUSTON MAMAY PROBLEM z uzytkownikiem");
                    //TODO catch code error
                }

            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Problem z polaczeniem");
                t.printStackTrace();
            }


        });
    }

}
