package com.example.heliao.projgo;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.Task;

import java.util.HashMap;

/**
 * Created by HeLiao on 4/14/2016.
 */
public class TaskDisplayFragment extends Fragment {
    TextView taskName, taskDescription,taskPeople,taskStartDate,taskEndDate,taskProjectName;
    EditText taskPrograss;
    Button donebutton;
    ServerDataManager dataManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = (View) inflater.inflate(R.layout.fragment_task_display, container, false);
        taskName = (TextView) rootview.findViewById(R.id.taskname_edittext_displaytask);
        taskProjectName = (TextView) rootview.findViewById(R.id.projectname_displaytask);
        taskDescription = (TextView) rootview.findViewById(R.id.description_edittext_displaytask);
        taskPeople = (TextView) rootview.findViewById(R.id.people_edittext_displaytask);
        taskStartDate = (TextView) rootview.findViewById(R.id.startdate_edittext_displaytask);
        taskEndDate = (TextView) rootview.findViewById(R.id.enddate_editText__displaytask);
        donebutton = (Button) rootview.findViewById(R.id.buttonDone_displayevent);
        taskPrograss=(EditText)rootview.findViewById(R.id.progess_displaytask);
        //final Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);

        Bundle bundle = this.getArguments();
        String eventType = bundle.getString("eventtype");
        String eventname = bundle.getString("eventname");
        dataManager = ServerDataManager.getInstance();

        Task selectedTask = dataManager.taskList.get(eventname);
        taskName.setText(selectedTask.name);
        taskDescription.setText(selectedTask.description);
        // List<String> participant = new ArrayList<String>();
        String taskParticipant = "";
        for(HashMap.Entry<String,String> entry : selectedTask.participant.entrySet()){
            taskParticipant +=entry.getValue() +" ";
        }
        taskPeople.setText(taskParticipant);
        taskStartDate.setText(selectedTask.start_time);
        taskEndDate.setText(selectedTask.end_time);

        taskProjectName.setText(selectedTask.project.name);



        final MainFragment mainFragment = new MainFragment();


        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(i);

                FragmentManager fragmentManager_mainfragment = getFragmentManager();
                FragmentTransaction fragmentTransaction_mainfragment = fragmentManager_mainfragment.beginTransaction();
                fragmentTransaction_mainfragment.replace(R.id.content_frame, mainFragment);
                fragmentTransaction_mainfragment.commit();

            }
        });



        return rootview;
    }
}
