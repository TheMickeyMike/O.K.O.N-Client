package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.AuthInterceptor;

public class MainActivity extends AppCompatActivity {


    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init LocalStore, we need to pass Context
        userLocalStore = new UserLocalStore(this);


        if (authenticate()) {
            startHomeActivity();
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                startHomeActivity();
                System.out.println("Login OK, Starting HomeActivity");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    private void startHomeActivity() {
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }



}
