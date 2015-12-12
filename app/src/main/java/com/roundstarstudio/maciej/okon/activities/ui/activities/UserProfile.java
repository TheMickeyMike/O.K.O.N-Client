package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.BuildConfig;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.AuthInterceptor;
import com.roundstarstudio.maciej.okon.activities.api.OkonAuthService;
import com.roundstarstudio.maciej.okon.activities.api.OkonService;
import com.roundstarstudio.maciej.okon.activities.api.model.AccessToken;
import com.roundstarstudio.maciej.okon.activities.api.model.Status;
import com.roundstarstudio.maciej.okon.activities.api.model.User;
import com.roundstarstudio.maciej.okon.activities.ui.adapters.OnLoadMoreListener;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class UserProfile extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView image;

    private int user_id;

    private UserLocalStore userLocalStore;
    private OkHttpClient authClient;



    // Recycler view
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmptyView;

    private List<Status> studentList;

    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        image = (ImageView) findViewById(R.id.backdrop);
        image.setImageResource(R.drawable.background_material);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user_id = getIntent().getExtras().getInt("USER_ID");

        //Init LocalStore, we need to pass Context
        userLocalStore = new UserLocalStore(this);

        if (authenticate()) {
            //Set up auth header for requests
            authClient = new AuthInterceptor(userLocalStore
                    .getAccessToken())
                    .getOkHttpClient();

            loadUserInfo();
            getUserProfile(null);

        } else {
            finish(); //TODO  Aby na pewno?
            //startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }







        //Init Recycler view
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<Status>();
        handler = new Handler();
//        loadData(null);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new DataAdapter(studentList, mRecyclerView,-1); //TODO

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

//        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

//        collapsingToolbar.setContentScrimColor(Color.BLUE);
//        collapsingToolbar.setStatusBarScrimColor(Color.GREEN);
        setPalette();

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {


                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.get(studentList.size() - 1).getId();
//                        int end = start + 20;

                        getUserProfile(start);
                    }
                });
            }
        });
    }


    private void setPalette() {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = ContextCompat.getColor(UserProfile.this, R.color.colorPrimaryDark);
                int primary = ContextCompat.getColor(UserProfile.this, R.color.colorPrimary);
                collapsingToolbar.setContentScrimColor(palette.getMutedColor(primary));
                collapsingToolbar.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(">>>>>>>>>> ON START");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        clearData();

        if (authenticate()) {
            //Set up auth header for requests
            authClient = new AuthInterceptor(userLocalStore
                    .getAccessToken())
                    .getOkHttpClient();

            loadUserInfo();
            getUserProfile(null);

        } else {
            finish(); //TODO  Aby na pewno?
            //startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        System.out.println("ON RESTARTTTTT");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("ON STOOOP");
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    public void clearData() {
        int size = this.studentList.size();
        if (size > 0) {
            studentList.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    private void getUserProfile(Integer id) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OkonService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OkonService okonAuthService = retrofit.create(OkonService.class);

        Call<List<Status>> call = okonAuthService.getPrivateFeed(user_id,2,null,id,null,null);

        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Response<List<Status>>response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    int statusCode = response.code();
                    List<Status> status = response.body();


                    for (Status _status : status) {
                        studentList.add(_status);
                        System.out.println(_status.getContent() + "  " + _status.getUser().getFullName());
                        mAdapter.notifyItemInserted(studentList.size());
                        mAdapter.setLoaded();
                    }


                    System.out.println(statusCode);
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



    private void loadUserInfo() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        // Set the custom client when building adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OkonService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(authClient)
                .build();

        OkonService okonService = retrofit.create(OkonService.class);
        Call<User> call = okonService.getUser(user_id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    int statusCode = response.code();
                    User user = response.body();

                    collapsingToolbar.setTitle(user.getFullName());

                    //Loading avatar in header
                    Glide.with(image.getContext())
                            .load(user.getGravatar_url())
                            .centerCrop()
                            .crossFade()
                            .into(image);


                    System.out.println(statusCode);

                } else if (response.code() == 401) {
                    System.out.println("401 w loadUserInfo");

                } else {
                    System.out.println("HIUSTON MAMAY PROBLEM z uzytkownikiem");
                    //TODO catch code error
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
