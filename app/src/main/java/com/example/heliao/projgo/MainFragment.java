package com.example.heliao.projgo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class MainFragment extends Fragment {

    CalendarFragment calendarfragment;
    TaskListFragment tasklistfragment;
    Button mCheckProject;
    TextView usernameDisplay;
    String username;
    String password;
    SharedPreferences sharedPreferences;
    ServerDataManager dataManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main,container,false);
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

        Bundle bundle = this.getArguments();
        try{
        if(!bundle.isEmpty()){
            username = bundle.getString("username");
            password = bundle.getString("password");
        }}catch (NullPointerException e){
            System.out.print(e.toString());
        }

        sharedPreferences= getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        username =  sharedPreferences.getString("nameKey", "miss");

        usernameDisplay = (TextView)rootview.findViewById(R.id.username_mainfragment);
        usernameDisplay.setText(username);
        mCheckProject = (Button)rootview.findViewById(R.id.checkProject);
        dataManager = ServerDataManager.getInstance();
        mCheckProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView projectList = new ListView(getActivity().getApplicationContext());
                //Find list of the existing project.
                List<String> projects = new ArrayList<String>();
                for(HashMap.Entry<String,Project> entry : dataManager.projectList.entrySet()){
                    projects.add(entry.getValue().name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.add_list_layout, R.id.addList_testview, projects);
                projectList.setAdapter(adapter);
                //Set up AlertDialog again
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setCancelable(true);
                dialogBuilder.setPositiveButton("OK", null);
                dialogBuilder.setView(projectList);
                final AlertDialog projectDialog = dialogBuilder.create();
                projectDialog.show();

                projectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        String selected = ((TextView) view.findViewById(R.id.addList_testview)).getText().toString();
                        bundle.putString("eventtype", "Project");
                        bundle.putString("eventname",selected);

                        ProjectDisplayFragment taskDetailFragment = new ProjectDisplayFragment();
                        taskDetailFragment.setArguments(bundle);
                        FragmentManager taskManager = getFragmentManager();
                        FragmentTransaction taskTranscation = taskManager.beginTransaction();
                        taskTranscation.replace(R.id.content_frame, taskDetailFragment);
                        taskTranscation.addToBackStack(null);
                        taskTranscation.commit();
                        projectDialog.cancel();
                    }
                });
            }
        });



        return rootview;
    }
}

