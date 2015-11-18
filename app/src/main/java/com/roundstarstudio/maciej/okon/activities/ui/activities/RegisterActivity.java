package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.OkonService;
import com.roundstarstudio.maciej.okon.activities.api.model.NewUser;
import com.roundstarstudio.maciej.okon.activities.api.model.Status;
import com.roundstarstudio.maciej.okon.activities.api.model.User;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;
    private EditText firstNameText, secondNameText, userNameText, emailText, passwordText;

    private  Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Init EditTexts
        firstNameText = (EditText) findViewById(R.id.firstNameText);
        secondNameText = (EditText) findViewById(R.id.secondNameText);
        userNameText = (EditText) findViewById(R.id.userNameText);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        // Init register Button
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                String firstName = firstNameText.getText().toString();
                String secondName = secondNameText.getText().toString();
                String userName = userNameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                NewUser user = new NewUser(firstName,secondName,userName,email,password);
                signUp(user);

                break;
        }
    }

    private void signUp(final NewUser user) {


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OkonService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OkonService apiService =
                retrofit.create(OkonService.class);

        Call<NewUser> call = apiService.signUp(user);

        call.enqueue(new Callback<NewUser>() {
            @Override
            public void onResponse(Response<NewUser> response,
                                   Retrofit retrofit) {

                if (response.isSuccess()) {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    returnIntent = new Intent();
                    returnIntent.putExtra(LoginActivity.REG_EMAIL,user.getEmail());
                    returnIntent.putExtra(LoginActivity.REG_PASS,user.getPassword());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish(); // Finish Activity


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
