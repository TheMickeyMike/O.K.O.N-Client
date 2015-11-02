package com.roundstarstudio.maciej.okon.activities.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.roundstarstudio.maciej.okon.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;
    private EditText firstNameText, secondNameText, userNameText, emailText, passwordText;

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

                User registerData = new User(firstName, secondName, userName, email, password);

                break;
        }
    }
}
