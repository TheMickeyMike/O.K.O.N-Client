package com.roundstarstudio.maciej.okon.activities.ui.activities.recyclerViewPt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.model.Status;

import java.util.List;

/**
 * Created by Maciej on 17.11.15.
 */
public class CardViewDataAdapter extends RecyclerView.Adapter<CardViewDataAdapter.ViewHolder>  {

    private List<Status> stList;

    public CardViewDataAdapter(List<Status> statuses) {
        this.stList = statuses;

    }


    // Create new views
    @Override
    public CardViewDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_row, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.tvName.setText(stList.get(position).getContent());

        viewHolder.tvEmailId.setText(stList.get(position).getUser().getFullName());

        viewHolder.singlestatus=stList.get(position);

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return stList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvEmailId;

        public Status singlestatus;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);

            tvEmailId = (TextView) itemLayoutView.findViewById(R.id.tvEmailId);
            // Onclick event for the row to show the data in toast
            itemLayoutView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Toast.makeText(
                            v.getContext(),
                            "Data : \n" + singlestatus.getContent() + " \n"
                                    + singlestatus.getUser().getFullName(),
                            Toast.LENGTH_SHORT).show();

                }
            });

        }

    }
}
