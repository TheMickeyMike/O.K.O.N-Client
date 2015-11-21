package com.roundstarstudio.maciej.okon.activities.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roundstarstudio.maciej.okon.R;

/**
 * Created by Maciej on 20.11.15.
 */
public class NewStatusFragment extends Fragment {

    NewStatusListener activityCommander;

    public interface NewStatusListener {
        public void createStatus(String content);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newstatuscard, container, false);
        return view;
    }
}

