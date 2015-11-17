package com.roundstarstudio.maciej.okon.activities.ui.activities.recyclerViewLast;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.model.Status;
import com.roundstarstudio.maciej.okon.activities.ui.adapters.OnLoadMoreListener;

import java.util.List;

/**
 * Created by Maciej on 17.11.15.
 */
public class OkonDataAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private static List<Status> imagesList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public OkonDataAdapter(List<Status> imagesList1, RecyclerView recyclerView) {
        imagesList = imagesList1;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return imagesList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cardview_row, parent, false);

            vh = new WallPaperViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof WallPaperViewHolder) {

            final Status singleWallPaper = (Status) imagesList.get(position);

            ((WallPaperViewHolder) holder).tvName.setText(singleWallPaper.getContent()+"");

            ((WallPaperViewHolder) holder).tvEmailId.setText(singleWallPaper.getUser().getFullName()+"");

            ((WallPaperViewHolder) holder).postion=position;
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class WallPaperViewHolder extends RecyclerView.ViewHolder {

//
//        public ImageView thumbIcon;
//
//        public TextView tvDownloads;
//
//        public TextView tvFav;

        public TextView tvName;

        public TextView tvEmailId;

        public Status student;

        public int postion;


        public WallPaperViewHolder(View v) {
            super(v);

//
//            thumbIcon = (ImageView) v.findViewById(R.id.thumbIcon);
//            tvDownloads = (TextView) v.findViewById(R.id.tvDownloads);
//            tvFav = (TextView) v.findViewById(R.id.tvFav);


            tvName = (TextView) v.findViewById(R.id.tvName);

            tvEmailId = (TextView) v.findViewById(R.id.tvEmailId);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(),
                            "OnClick :" + student.getContent() + " \n " + student.getUser().getFullName(),
                            Toast.LENGTH_SHORT).show();

                }
            });



        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}
