package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.roundstarstudio.maciej.okon.R;
import com.roundstarstudio.maciej.okon.activities.api.model.Connections;
import com.roundstarstudio.maciej.okon.activities.api.model.Status;
import com.roundstarstudio.maciej.okon.activities.ui.adapters.OnLoadMoreListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.SimpleFormatter;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Maciej on 16.11.15.
 */
public class DataAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Status> studentList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private int user_id;


    public DataAdapter(List<Status> students, RecyclerView recyclerView) {

        this.studentList = students;


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
        return studentList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.statuscardview, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {
            //Hot Fix
            ((StudentViewHolder) holder).editTV.setVisibility(View.GONE);
            ((StudentViewHolder) holder).edited.setVisibility(View.GONE);

            Status singleStudent = (Status) studentList.get(position);

            ((StudentViewHolder) holder).tvContent.setText(singleStudent.getContent());

            ((StudentViewHolder) holder).tvFullName.setText(singleStudent.getUser().getFullName());

            ((StudentViewHolder) holder).tvUserName.setText("@" + singleStudent.getUser().getUsername());

            ((StudentViewHolder) holder).tvDate.setText(ConvertDate(singleStudent.getCreatedAt()));

            ((StudentViewHolder) holder).student = singleStudent;
            System.out.println(singleStudent.getUser().getId());


            if (singleStudent.getUser().getId() == user_id) {
                System.out.println("I m IN");
                        ((StudentViewHolder) holder).editTV.setVisibility(View.VISIBLE);
            }

            if (!singleStudent.getCreatedAt().equals(singleStudent.getUpdatedAt())) {
                ((StudentViewHolder) holder).edited.setVisibility(View.VISIBLE);
            }

            Glide.with(((StudentViewHolder) holder).avatar.getContext())
                    .load(singleStudent.getUser().getGravatar_url())
                    .centerCrop()
                    .crossFade()
                    .into(((StudentViewHolder) holder).avatar);


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContent;

        public TextView tvFullName;

        public TextView tvUserName;

        public TextView tvDate;

        public TextView editTV;

        public Status student;

        public CircleImageView avatar;

        //NewStatusCard
        public EditText newContentET;
        public CircleImageView avatarNC;
        public TextView nameNC;
        TextView userNameNC, edited;

        public StudentViewHolder(View v) {
            super(v);

            tvContent = (TextView) v.findViewById(R.id.content);

            tvFullName = (TextView) v.findViewById(R.id.full_name);

            tvUserName = (TextView) v.findViewById(R.id.user_name);

            tvDate = (TextView) v.findViewById(R.id.date);

            editTV = (TextView) v.findViewById(R.id.editTV);

            avatar = (CircleImageView) v.findViewById(R.id.profile_image);


            //Init newStatus Card
            newContentET = (EditText) v.findViewById(R.id.newContentET);
            avatarNC = (CircleImageView) v.findViewById(R.id.avatarNC);
            nameNC = (TextView) v.findViewById(R.id.nameNC);
            userNameNC = (TextView) v.findViewById(R.id.userNameNC);
            edited = (TextView) v.findViewById(R.id.edited);

            Typeface roboto_light = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Geometria/Geometria-Light.otf");
            Typeface roboto_medium = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Geometria/Geometria-Medium.otf");
            Typeface roboto_thin = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Geometria/Geometria-Thin.otf");
            this.tvFullName.setTypeface(roboto_medium);
            this.tvContent.setTypeface(roboto_light);
            this.tvUserName.setTypeface(roboto_light);
            this.tvDate.setTypeface(roboto_light);
            this.edited.setTypeface(roboto_thin);

            editTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("CLICK");
                    Intent intent = new Intent(v.getContext(), PopNewStatus.class);
                    intent.putExtra("requestCode", HomeActivity.EDIT_STATUS_REQUEST);
                    intent.putExtra("USER_ID",student.getId());
                    intent.putExtra("TEXT", student.getContent());
                            ((Activity) v.getContext()).startActivityForResult(intent, HomeActivity.EDIT_STATUS_REQUEST);
                }
            });


            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(),
                            "OnClick :" + student.getContent() + " \n " + student.getUser().getFullName(),
                            Toast.LENGTH_SHORT).show();

                    Context context = v.getContext();

                    Intent intent = new Intent(context, UserProfile.class);
                    intent.putExtra("USER_ID", student.getUser().getId());
                    //TODO Put extra ID
                    context.startActivity(intent);
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


    private String ConvertDate(String raw) {


        DateTimeZone utc = DateTimeZone.UTC;

        Calendar cal = Calendar.getInstance();

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        // DateTime dt1 = format.parseDateTime(raw).withZone(utc);
        DateTime dt1 = new DateTime(raw);
        DateTime dt2 = new DateTime(cal);

        int daysBetween = Days.daysBetween(dt1, dt2).getDays();
        int hoursBetween = Hours.hoursBetween(dt1, dt2).getHours() % 24;
        int minutesBetween = Minutes.minutesBetween(dt1, dt2).getMinutes() % 60;
        int secondsBetween = Seconds.secondsBetween(dt1, dt2).getSeconds() % 60;
//        System.out.println(daysBetween + " " + hoursBetween + " " + minutesBetween + " " + secondsBetween);

        if (daysBetween > 0) {
            if (daysBetween == 1)
                return daysBetween + "d";
            else
                return daysBetween + "d";
        } else if (hoursBetween > 0) {
            if (hoursBetween == 1)
                return hoursBetween + "h";
            else
                return hoursBetween + "h";
        } else if (minutesBetween > 0) {
            if (minutesBetween == 1)
                return minutesBetween + "m";
            else
                return minutesBetween + "m";
        } else if (secondsBetween > 0) {
            if (secondsBetween == 1)
                return secondsBetween + "s";
            else
                return secondsBetween + "s";
        } else return null;
    }
}
