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

/**
 * Created by HeLiao on 3/15/2016.
 */
public class TaskListFragment extends Fragment {
    TextView tasklistTitle;
    FragmentConnector fragmentConnector;
    FragmentManager fragmentManager_tasklist;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            fragmentConnector = (FragmentConnector) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the fragment layout file
        View rootView =(View)inflater.inflate(R.layout.fragment_tasklistview, container, false);

        final ListView taskListView =(ListView)rootView.findViewById(R.id.tasklistView);
       tasklistTitle = (TextView)rootView.findViewById(R.id.tasklistTitle);

        //create datasource
        String[] datasource = {"play starcraft","go to the beach","hiking"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.rowlayout,R.id.txtitem,datasource);
        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup vg =(ViewGroup)view;
                TextView textView = (TextView) vg.findViewById(R.id.txtitem);
                String title = textView.getText().toString();
                //fragmentConnector.getValueFromFragmentUsingInterface(title);
                Toast.makeText(getActivity().getApplicationContext(),title,Toast.LENGTH_LONG).show();
                Fragment taskDetailFragment = new TaskDetailFragment();
                fragmentManager_tasklist = getFragmentManager();
                FragmentTransaction transaction = fragmentManager_tasklist.beginTransaction();
                transaction.replace(R.id.content_frame,taskDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });

        return rootView;
    }

    public void updateinfo(int year, int month, int day){
        tasklistTitle.setText(month+"/"+day+"/"+year);
    }

}
