package com.example.heliao.projgo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.heliao.projgo.projgoServerData.Conference;
import com.example.heliao.projgo.projgoServerData.Project;
import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.Task;
import com.example.heliao.projgo.projgoServerData.User;

import java.util.HashMap;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class ProjectDisplayFragment extends Fragment{
    TextView eventName, eventLabel,eventDescription,eventPeople,eventStartDate,eventEndDate;
    Button donebutton,modifyButton,deleteButton;
    ServerDataManager dataManager;
    Project selectedProject;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = (View)inflater.inflate(R.layout.fragment_project_display,container,false);
        eventName = (TextView)rootview.findViewById(R.id.eventname_edittext_displayevent);
        eventLabel = (TextView)rootview.findViewById(R.id.eventLabel_displayevent);
        eventDescription=(TextView)rootview.findViewById(R.id.description_edittext_displayevent);
        eventPeople = (TextView)rootview.findViewById(R.id.people_edittext_displayevent);
        eventStartDate = (TextView)rootview.findViewById(R.id.startdate_edittext_displayevent);
        eventEndDate = (TextView)rootview.findViewById(R.id.enddate_editText__displayevent);


        donebutton = (Button)rootview.findViewById(R.id.buttonDone_displayevent);
        modifyButton = (Button) rootview.findViewById(R.id.modify_projectdisplay);
        deleteButton = (Button) rootview.findViewById(R.id.delete_projectdisplay);
        //final Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);

        Bundle bundle = this.getArguments();
        String eventType = bundle.getString("eventtype");
        eventLabel.setText(eventType);
        String eventname = bundle.getString("eventname");
        dataManager= ServerDataManager.getInstance();

        switch (eventType){
            case "Project":
                selectedProject = dataManager.projectList.get(eventname);
                eventName.setText(selectedProject.name);
                eventDescription.setText(selectedProject.description);
               // List<String> participant = new ArrayList<String>();
                String participant = "";
                for(HashMap.Entry<String,String> entry : selectedProject.participant.entrySet()){
                    participant +=entry.getValue() +" ";
                }
                eventPeople.setText(participant);
                eventStartDate.setText(selectedProject.startdate);
                eventEndDate.setText(selectedProject.enddate);
                break;
            case "Task":
                Task selectedTask = dataManager.taskList.get(eventname);
                eventName.setText(selectedTask.name);
                eventDescription.setText(selectedTask.description);
                // List<String> participant = new ArrayList<String>();
                String taskParticipant = "";
                for(HashMap.Entry<String,String> entry : selectedTask.participant.entrySet()){
                    taskParticipant +=entry.getValue() +" ";
                }
                eventPeople.setText(taskParticipant);
                eventStartDate.setText(selectedTask.start_time);
                eventEndDate.setText(selectedTask.end_time);
                break;
            case "Conference":
                Conference selectedConference = dataManager.conferenceList.get(eventname);
                eventName.setText(selectedConference.name);
                eventDescription.setText(selectedConference.description);
                // List<String> participant = new ArrayList<String>();
                String conParticipant = "";
                for(HashMap.Entry<String,String> entry : selectedConference.participant.entrySet()){
                    conParticipant +=entry.getValue() +" ";
                }
                eventPeople.setText(conParticipant);
                eventStartDate.setText(selectedConference.start_time);
                eventEndDate.setText(selectedConference.end_time);
                break;
        }



        final MainFragment mainFragment = new MainFragment();


        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(i);

                FragmentManager fragmentManager_mainfragment = getFragmentManager();
                FragmentTransaction fragmentTransaction_mainfragment = fragmentManager_mainfragment.beginTransaction();
                fragmentTransaction_mainfragment.replace(R.id.content_frame,mainFragment);
                fragmentTransaction_mainfragment.commit();

            }
        });

        //if(curr)



        return rootview;
    }

}
