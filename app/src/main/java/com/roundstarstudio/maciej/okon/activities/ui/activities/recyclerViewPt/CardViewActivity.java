package com.roundstarstudio.maciej.okon.activities.ui.activities.recyclerViewPt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.OkonService;
import com.roundstarstudio.maciej.okon.activities.api.model.Status;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Maciej on 17.11.15.
 */
public class CardViewActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Status> statusList;

    // on scroll

    private static int current_page = 1;

    private int ival = 1;
    private int loadLimit = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_activity);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statusList = new ArrayList<Status>();


        loadMoreData(null);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new CardViewDataAdapter(statusList);


        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(
                mLayoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                // do somthing...

                loadMoreData(current_page);

            }

        });

    }

    // By default, we add 10 objects for first time.
//    private void loadData(int current_page) {
//
//        // I have not used current page for showing demo, if u use a webservice
//        // then it is useful for every call request
//
//        for (int i = ival; i <= loadLimit; i++) {
//            Student st = new Student("Student " + i, "androidstudent" + i
//                    + "@gmail.com", false);
//
//            studentList.add(st);
//            ival++;
//
//        }
//
//    }


    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(Integer current_page) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request

//        loadLimit = ival + 10;
//
//        for (int i = ival; i <= loadLimit; i++) {
//            Student st = new Student("Student " + i, "androidstudent" + i
//                    + "@gmail.com", false);
//
//            studentList.add(st);
//            ival++;
//        }
//
//        mAdapter.notifyDataSetChanged();


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
        Call<List<Status>> call =  okonService.getFeed(2,null,current_page);

        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Response<List<Status>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    int statusCode = response.code();
                    List<Status> statuses = response.body();


                    for (Status _status : statuses) {
                        statusList.add(_status);
                        System.out.println(_status.getContent() + "  " + _status.getUser().getFullName());
//                        mAdapter.notifyItemInserted(statusList.size());
//                        mAdapter.setLoaded();
                    }

                    mAdapter.notifyDataSetChanged();


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



