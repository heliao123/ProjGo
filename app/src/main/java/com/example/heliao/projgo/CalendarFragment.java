package com.example.heliao.projgo;

import android.app.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

/**
 * Created by HeLiao on 3/15/2016.
 */
public class CalendarFragment extends Fragment {


    private FragmentConnector fragmentConnector;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            fragmentConnector = (FragmentConnector) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview =(View)inflater.inflate(R.layout.fragment_calendar,container,false);
        final CalendarView calendar = (CalendarView) rootview.findViewById(R.id.calendarview);
        //Passing in year, month, day into the interface
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                fragmentConnector.getValueFromFragmentUsingInterface(year,month+1,dayOfMonth);
            }
        });
        return rootview;

    }
}
