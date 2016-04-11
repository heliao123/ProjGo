package com.example.heliao.projgo;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heliao.projgo.projgoServerData.Conference;
import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HeLiao on 3/15/2016.
 */
public class TaskListFragment extends Fragment {
    TextView tasklistTitle;
    FragmentConnector fragmentConnector;
    FragmentManager fragmentManager_tasklist;
    ServerDataManager dataManager;
    String[] tasklist={};
    LayoutInflater mInflater;
    ViewGroup mContainer;
    String selectedDate;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentConnector = (FragmentConnector) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the fragment layout file
        mInflater = inflater;
        mContainer = container;
        View rootView = (View) inflater.inflate(R.layout.fragment_tasklistview, container, false);

        final ListView taskListView = (ListView) rootView.findViewById(R.id.tasklistView);
        tasklistTitle = (TextView) rootView.findViewById(R.id.tasklistTitle);
        /*Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c.getTime());*/
        tasklistTitle.setText(selectedDate);

        //create datasource
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.rowlayout, R.id.txtitem, tasklist);
        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup vg = (ViewGroup) view;
                TextView textView = (TextView) vg.findViewById(R.id.txtitem);
                String title = textView.getText().toString();
                //fragmentConnector.getValueFromFragmentUsingInterface(title);
                Bundle bundle = new Bundle();
                if(dataManager.taskList.containsKey(title)){
                    bundle.putString("eventtype","Task");
                    bundle.putString("eventname",title);
                }else{
                    bundle.putString("eventtype","Conference");
                    bundle.putString("eventname",title);
                }
                Fragment taskDetailFragment = new TaskDetailFragment();
                taskDetailFragment.setArguments(bundle);
                fragmentManager_tasklist = getFragmentManager();
                FragmentTransaction transaction = fragmentManager_tasklist.beginTransaction();
                transaction.replace(R.id.content_frame, taskDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    public void updateinfo(int year, int month, int day) {
        selectedDate = month + "/" + day + "/" + year;
        tasklist = createTaskList(selectedDate).toArray(new String[]{});
        Fragment reloadFragment = getFragmentManager().findFragmentById(R.id.listview_fragmentcontainer);
        FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
        fragTrans.detach(reloadFragment);
        fragTrans.attach(reloadFragment);
        fragTrans.commit();

        tasklistTitle.setText(selectedDate);
    }

    public List<String> createTaskList(String date) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        List<String> tasks = new ArrayList<String>();
        dataManager = ServerDataManager.getInstance();
        for (HashMap.Entry<String, Task> entry : dataManager.taskList.entrySet()) {
            try {
                Date startDate = df.parse(entry.getValue().start_time);
                Date endDate = df.parse(entry.getValue().end_time);
                Date currentDate = df.parse(date);
                startDate = new Date(startDate.getTime() - 1*24*3600*1000);
                endDate = new Date(endDate.getTime() + 1*24*3600*1000);

                if (currentDate.after(startDate) && currentDate.before(endDate)) {
                    tasks.add(entry.getValue().name);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (HashMap.Entry<String, Conference> entry : dataManager.conferenceList.entrySet()) {
            try {
                Date startDate = df.parse(entry.getValue().start_time);
                Date endDate = df.parse(entry.getValue().end_time);
                Date currentDate = df.parse(date);
                startDate = new Date(startDate.getTime() - 1*24*3600*1000);
                endDate = new Date(endDate.getTime() + 1*24*3600*1000);

                if (currentDate.after(startDate) && currentDate.before(endDate)) {
                    tasks.add(entry.getValue().name);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return tasks;


    }

}
