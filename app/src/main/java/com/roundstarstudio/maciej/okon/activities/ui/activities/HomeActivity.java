package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.AuthInterceptor;
import com.roundstarstudio.maciej.okon.activities.api.OkonService;
import com.roundstarstudio.maciej.okon.activities.api.model.*;
import com.roundstarstudio.maciej.okon.activities.api.model.NewStatus;
import com.roundstarstudio.maciej.okon.activities.ui.activities.recyclerViewLast.recActivity;
import com.roundstarstudio.maciej.okon.activities.ui.activities.recyclerViewPt.CardViewActivity;
import com.roundstarstudio.maciej.okon.activities.ui.adapters.OnLoadMoreListener;
import com.squareup.okhttp.OkHttpClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnLoadMoreListener, View.OnClickListener{

    static final int NEW_STATUS_REQUEST = 1;  // The request code
    static final int EDIT_STATUS_REQUEST = 2;  // The request code

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int my_id = 0;
    //NewStatusCard
    EditText newContentET;
    CircleImageView avatarNC;
    TextView nameNC;
    TextView userNameNC;


    //Avatar in header View
    CircleImageView avatar;

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View header;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView usernameTV, emailTV;

    private CoordinatorLayout coordinatorLayout;

    private UserLocalStore userLocalStore;

    private OkHttpClient authClient;

    // Recycler view
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmptyView;

    private List<Status> studentList;

    protected Handler handler;

    public FloatingActionButton fab, fabSend;

    private Typeface roboto_light, roboto_medium, roboto_bold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Init fonts
        roboto_light = Typeface.createFromAsset(getAssets(), "fonts/Geometria/Geometria-Light.otf");
        roboto_bold = Typeface.createFromAsset(getAssets(), "fonts/Geometria/Geometria-Bold.otf");
        roboto_medium = Typeface.createFromAsset(getAssets(), "fonts/Geometria/Geometria-Medium.otf");


        //Init LocalStore, we need to pass Context
        userLocalStore = new UserLocalStore(this);

        //Init HttpClient
        authClient = new AuthInterceptor(userLocalStore
                .getAccessToken())
                .getOkHttpClient();


        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Feed");
        setSupportActionBar(toolbar);

        // Init Swipe to refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);


        /**
         * Init Recycler View
         */

        //Init Recycler view
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<Status>();
        //Init handler for onScrollListener
        handler = new Handler();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new DataAdapter(studentList, mRecyclerView);
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);


        //Set OnLoadMore Listener
        mAdapter.setOnLoadMoreListener(this);

        loadUserInfo();


        loadData(null);



        //Init HomeActivity CoordinatorLayout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);



        /*
         * Init NavigationView
         */

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(this);
        //Init header in navigation View
        header = navigationView.inflateHeaderView(R.layout.header);
        //Init username, email, avatar in header View
        avatar = (CircleImageView) header.findViewById(R.id.profile_image);
        usernameTV = (TextView) header.findViewById(R.id.usernameTeV);
        emailTV = (TextView) header.findViewById(R.id.emailTeV);
        //Set Font and
        usernameTV.setTypeface(roboto_bold);
        emailTV.setTypeface(roboto_light);

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView.getMenu().findItem(R.id.feed).setChecked(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.openDrawer,
                R.string.closeDrawer);


        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


        //Init newStatus Card
        newContentET = (EditText) findViewById(R.id.newContentET);
        avatarNC = (CircleImageView) findViewById(R.id.avatarNC);
        nameNC = (TextView) findViewById(R.id.nameNC);
        userNameNC = (TextView) findViewById(R.id.userNameNC);


        //Init FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabSend = (FloatingActionButton) findViewById(R.id.fabSend);


        //Set OnClick FAB
        fabSend.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.ic_send_white_24dp));
        fabSend.setOnClickListener(this);
        fab.setOnClickListener(this);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int size = studentList.size();
                        studentList.clear();
                        mAdapter.notifyItemRangeRemoved(0, size);
                        loadData(null);
                        mSwipeRefreshLayout.setRefreshing(false);


                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("USSSSEEERRR ID :" + my_id);
        System.out.println(">>>>>>>>>> ON START");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //clearData();  //TODO
        System.out.println("ON RESTARTTTTT");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("ON STOOOP");
    }

    public void clearData() {
        int size = this.studentList.size();
        if (size > 0) {
            studentList.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
        }
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {


            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.feed:
                Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
//                        ContentFragment fragment = new ContentFragment();
//                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.frame, fragment);
//                        fragmentTransaction.commit();
                return true;

            // For rest of the options we just show a toast on click

            case R.id.starred:
                Toast.makeText(getApplicationContext(), "Stared Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sent_mail:
                Toast.makeText(getApplicationContext(), "Send Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.drafts:
                Toast.makeText(getApplicationContext(), "Drafts Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.allmail:
                Toast.makeText(getApplicationContext(), "All Mail Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.trash:
                Toast.makeText(getApplicationContext(), "Trash Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.spam:
                Toast.makeText(getApplicationContext(), "Spam Selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;

        }
    }


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

                loadData(start);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(this, PopNewStatus.class);
                intent.putExtra("requestCode", NEW_STATUS_REQUEST);
                startActivityForResult(intent, NEW_STATUS_REQUEST);

                //startActivity(new Intent(this,PopNewStatus.class));
//                findViewById(R.id.newStatus).setVisibility(View.VISIBLE);
//                fab.setVisibility(View.GONE);
//                fabSend.setVisibility(View.VISIBLE);
                break;
            case R.id.fabSend:
                com.roundstarstudio.maciej.okon.activities.api.model.NewStatus status =
                        new com.roundstarstudio.maciej.okon.activities.api.model.NewStatus(newContentET.getText().toString());
                sendToServer(status);

                findViewById(R.id.newStatus).setVisibility(View.GONE);
                fabSend.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_STATUS_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String newStatusContent = data.getStringExtra("CONTENT");
                System.out.println(">>>>>>>>>>>>ON RESULT");
                sendToServer(new NewStatus(newStatusContent));
                System.out.println(newStatusContent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == EDIT_STATUS_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String newStatusContent = data.getStringExtra("CONTENT");
                int status_id = data.getIntExtra("ID",-1);
                System.out.println(">>>>>>>>>>>>ON RESULT");
                updateStatus(status_id,new NewStatus(newStatusContent));
                System.out.println("Updating status: "  + newStatusContent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }



    // load initial data
    private void loadData(Integer id) {


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
        Call<List<Status>> call = okonService.getFeed(2, null, id);  //TODO Zmienic limit z 2 !!!

        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Response<List<Status>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    int statusCode = response.code();
                    List<Status> statuses = response.body();


                    for (Status _status : statuses) {
                        studentList.add(_status);
                        System.out.println(_status.getContent() + "  " + _status.getUser().getFullName());
                        mAdapter.notifyItemInserted(studentList.size());
                        mAdapter.setLoaded();
                    }


                    System.out.println(statusCode);

                } else if (response.code() == 401) {
                    RevokeUserToken();
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
        Call<User> call = okonService.getMe();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    int statusCode = response.code();
                    User user = response.body();
                    mAdapter.setUser_id(user.getId());
                    my_id = user.getId();
                    System.out.println("FROM METHOD USER ID" + my_id);
                    System.out.println(user.getFullName() + "  " + user.getEmail());

                    usernameTV.setText(user.getFullName());
                    emailTV.setText(user.getEmail());


                    //Loading avatar in header
                    Glide.with(avatar.getContext())
                            .load(user.getGravatar_url())
                            .centerCrop()
                            .crossFade()
                            .into(avatar);


                    //NewStatusCard text

                    nameNC.setText(user.getFullName());
                    userNameNC.setText(user.getUsername());

                    Glide.with(avatar.getContext())
                            .load(user.getGravatar_url())
                            .centerCrop()
                            .crossFade()
                            .into(avatarNC);


                    System.out.println(statusCode);

                } else if (response.code() == 401) {
                    System.out.println("401 w loadUserInfo");

                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Blad pobierania danych uzytkownika!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    System.out.println("HIUSTON MAMAY PROBLEM z uzytkownikiem");
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

    private void RevokeUserToken() {
        System.out.println("REVOKE USER TOKEN: 401");
//        studentList.clear();
        //Revoke access token
        userLocalStore.clearUserData();
        onStart();
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
                    Toast.makeText(HomeActivity.this,
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

    private void updateStatus(int status_id, com.roundstarstudio.maciej.okon.activities.api.model.NewStatus status) {

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

        Call<Boolean> call = apiService.updateStatus(Integer.toString(status_id),status);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Response<Boolean> response,
                                   Retrofit retrofit) {

                if (response.isSuccess()) {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    Toast.makeText(HomeActivity.this,
                            "Edytowano!", Toast.LENGTH_SHORT).show();

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
