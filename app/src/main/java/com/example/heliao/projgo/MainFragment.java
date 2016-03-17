package com.example.heliao.projgo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class MainFragment extends Fragment {

    CalendarFragment calendarfragment;
    TaskListFragment tasklistfragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        calendarfragment = new CalendarFragment();
        tasklistfragment = new TaskListFragment();

        FragmentManager fragmentManager_calendar = getFragmentManager();
        FragmentTransaction fragmentTransaction_calendar = fragmentManager_calendar.beginTransaction();
        fragmentTransaction_calendar.add(R.id.calendar_fragmentcontainer, calendarfragment);
        fragmentTransaction_calendar.commit();


        FragmentManager fragmentManager_listview = getFragmentManager();
        FragmentTransaction fragmentTransaction_listview = fragmentManager_listview.beginTransaction();
        fragmentTransaction_listview.add(R.id.listview_fragmentcontainer, tasklistfragment);
        fragmentTransaction_listview.commit();
        return view;
    }
}

