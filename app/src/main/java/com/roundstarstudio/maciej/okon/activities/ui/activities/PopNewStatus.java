package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.AuthInterceptor;
import com.roundstarstudio.maciej.okon.activities.api.OkonService;
import com.roundstarstudio.maciej.okon.activities.api.model.*;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PopNewStatus extends Activity implements View.OnClickListener{

    private Button shareBtn;

    private EditText content;

    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String user_id = getIntent().getExtras().getString("USER_ID");
        
        setContentView(R.layout.activity_pop_new_status);

        //Init LocalStore, we need to pass Context
        userLocalStore = new UserLocalStore(this);

        content = (EditText) findViewById(R.id.newContentET);

        shareBtn = (Button) findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

//        getWindow().setLayout((int) (width*.9),(int) (height*.6));
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareBtn:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("CONTENT",content.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish(); // Finish Activity
                break;
        }
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
                    Toast.makeText(PopNewStatus.this,
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
