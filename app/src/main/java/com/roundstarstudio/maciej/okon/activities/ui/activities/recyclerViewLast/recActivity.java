package com.roundstarstudio.maciej.okon.activities.ui.activities.recyclerViewLast;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.OkonService;
import com.roundstarstudio.maciej.okon.activities.api.model.Status;
import com.roundstarstudio.maciej.okon.activities.ui.activities.DataAdapter;
import com.roundstarstudio.maciej.okon.activities.ui.adapters.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class recActivity extends AppCompatActivity {

    private static final String TAG = "WallpaperActivity";

    private RecyclerView mRecyclerView;
    private OkonDataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    public static int pageNumber;

    private List<Status> wallpaperImagesList;


    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        pageNumber = 1;
        wallpaperImagesList = new ArrayList<Status>();
        handler = new Handler();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);


        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);


        // create an Object for Adapter
        mAdapter = new OkonDataAdapter(wallpaperImagesList, mRecyclerView);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);


        getWebServiceData();

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                wallpaperImagesList.add(null);
                mAdapter.notifyItemInserted(wallpaperImagesList.size() - 1);
                ++pageNumber;

                wallpaperImagesList.remove(wallpaperImagesList.size() - 1);
                mAdapter.notifyItemRemoved(wallpaperImagesList.size());

                getWebServiceData();


            }
        });


    }


    public void getWebServiceData() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        // Set the custom client when building adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OkonService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(authClient)
                .build();

        OkonService okonService = retrofit.create(OkonService.class);
        Call<List<Status>> call =  okonService.getFeed(2,null,null);

        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Response<List<Status>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    int statusCode = response.code();
                    List<Status> statuses = response.body();


                    for (Status _status : statuses) {
                        wallpaperImagesList.add(_status);
                        System.out.println(_status.getContent() + "  " + _status.getUser().getFullName());
//                        mAdapter.notifyItemInserted(statusList.size());
//                        mAdapter.setLoaded();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemInserted(wallpaperImagesList.size());


                        }
                    });


                    System.out.println(statusCode);

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


