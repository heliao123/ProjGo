package com.example.heliao.projgo;

import android.app.Fragment;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the fragment layout file
        View rootView =(View)inflater.inflate(R.layout.fragment_tasklistview, container, false);

        ListView taskListView =(ListView)rootView.findViewById(R.id.tasklistView);

        //create datasource
        String[] datasource = {"play starcraft","go to the beach","hiking"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_expandable_list_item_1,datasource);
        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),view.toString(),Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

}
