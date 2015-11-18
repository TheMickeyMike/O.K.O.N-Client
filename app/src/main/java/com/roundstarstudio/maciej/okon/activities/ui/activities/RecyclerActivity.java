package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.OkonService;
import com.roundstarstudio.maciej.okon.activities.api.model.Status;
import com.roundstarstudio.maciej.okon.activities.api.model.User;
import com.roundstarstudio.maciej.okon.activities.ui.adapters.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class RecyclerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmptyView;
    private Toolbar toolbar;

    private List<Status> studentList;


    protected Handler handler;


    private Integer current_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_vieww);
        studentList = new ArrayList<Status>();
        handler = new Handler();

        loadData(null);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new DataAdapter(studentList, mRecyclerView);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();


//        if (studentList.isEmpty()) {
//            mRecyclerView.setVisibility(View.GONE);
//            tvEmptyView.setVisibility(View.VISIBLE);
//            System.out.println("GONE");
//
//        } else {
//            mRecyclerView.setVisibility(View.VISIBLE);
//            tvEmptyView.setVisibility(View.GONE);
//            System.out.println("VISABLE");
//        }

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
                        int end = start + 20;

                        loadData(start);

                    }
                });


//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //   remove progress item
//                        studentList.remove(studentList.size() - 1);
//                        mAdapter.notifyItemRemoved(studentList.size());
//                        //add items one by one
//                        int start = studentList.get(studentList.size() - 1).getId();
//                        int end = start + 20;
//
//                        loadData(start);
////
////                        for (int i = start + 1; i <= end; i++) {
////                            studentList.add(new Status("Student " + i,  i ));
////                            mAdapter.notifyItemInserted(studentList.size());
////                        }
////                        mAdapter.setLoaded();
//                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                    }
//                }, 2000);

            }
        });
    }


    private void checkInitData() {
        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
            System.out.println("GONE");

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
            System.out.println("VISABLE");
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
//                .client(authClient)
                .build();

        OkonService okonService = retrofit.create(OkonService.class);
        Call<List<Status>> call =  okonService.getFeed(2,null,id);  //TODO Zmienic limit z 2 !!!

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
