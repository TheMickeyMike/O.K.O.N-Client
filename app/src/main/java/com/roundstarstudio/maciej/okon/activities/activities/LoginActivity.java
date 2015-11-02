package com.roundstarstudio.maciej.okon.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.roundstarstudio.maciej.okon.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button logInButton;
    private TextView registerLink;

    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        // Initializing Log In button and add Click Listener
        logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(this);

        //Init register link
        registerLink = (TextView) (findViewById(R.id.registerLinkTextView));
        registerLink.setOnClickListener(this);

        //Init LocalStore, we need to pass Context
        userLocalStore = new UserLocalStore(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logInButton:


                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.registerLinkTextView:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }

    }

    //Start new Intent MainActivity
    private void startMainActivity() {
        Intent i = new Intent(this,MainActivity.class);
        EditText emailText = (EditText) findViewById(R.id.emailText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);

        startActivity(i);
    }

}
