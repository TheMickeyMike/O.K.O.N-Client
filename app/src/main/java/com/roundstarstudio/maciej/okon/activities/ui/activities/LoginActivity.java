package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.BuildConfig;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.OkonAuthService;
import com.roundstarstudio.maciej.okon.activities.api.model.AccessToken;
import com.roundstarstudio.maciej.okon.activities.api.model.Connections;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logInButton;
    private TextView registerLink;
    private EditText emailText, passwordText;
    private CoordinatorLayout coordinatorLayout;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;

    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //Init layout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);


        // Initializing Log In button and add Click Listener
        logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(this);

        //Init register link
        registerLink = (TextView) (findViewById(R.id.registerLinkTextView));
        registerLink.setOnClickListener(this);

        //Init LocalStore, we need to pass Context
        userLocalStore = new UserLocalStore(this);

        //Init passwordTextInputLayout and emailTextInputLayout
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);

        //Init EditTexts
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        //Set validator to email and password
        emailText.addTextChangedListener(new MyTextWatcher(emailText));
        passwordText.addTextChangedListener(new MyTextWatcher(passwordText));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logInButton:
                if (submitForm()) {  // Validate email and password
                    String email = emailText.getText().toString();
                    String password = passwordText.getText().toString();
                    getAccessToken(email, password);
                }
                break;
            case R.id.registerLinkTextView:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }

    }

    //Get AccessToken form OAuth
    private void getAccessToken(String email, String password) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OkonAuthService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OkonAuthService okonAuthService = retrofit.create(OkonAuthService.class);

        Call<AccessToken> call = okonAuthService.getAccessToken(BuildConfig.CLIENT_KEY, BuildConfig.CLIENT_SECRET, BuildConfig.GRANT_TYPE, email, password);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Response<AccessToken> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    int statusCode = response.code();
                    AccessToken accessToken = response.body();

                    // Save token to shared prefernces
                    userLocalStore.storeAccessToken(accessToken.getAccess_token());
                    userLocalStore.setUserLoggedIn(true);

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);

                    System.out.println(statusCode);
                    System.out.println(accessToken.getAccess_token() + "   " + accessToken.getToken_type() + "  ");

                    finish(); // Finish Activity


                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Blad logowania!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    System.out.println("HIUSTON MAMAY PROBLEM");
                    //TODO catch code error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Problem z nawiazaniem polaczenia.", Snackbar.LENGTH_LONG);
                snackbar.show();
                t.printStackTrace();
            }
        });
    }


    /**
     * Validating form
     */
    private boolean submitForm() {

        if (!validateEmail()) {
            return false;
        }

        if (!validatePassword()) {
            return false;
        }

//        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        return true;
    }



    private boolean validateEmail() {
        String email = emailText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(emailText);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (passwordText.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(passwordText);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.emailText:
                    validateEmail();
                    break;
                case R.id.passwordText:
                    validatePassword();
                    break;
            }
        }
    }

}
