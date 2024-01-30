package com.example.cs2340_first_project.ui.weeklyschedule;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cs2340_first_project.R;

public class WeeklyScheduleFragment extends Fragment {

    private WeeklyScheduleViewModel mViewModel;

    public static WeeklyScheduleFragment newInstance() {
        return new WeeklyScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WeeklyScheduleViewModel.class);
        // TODO: Use the ViewModel
    }

}