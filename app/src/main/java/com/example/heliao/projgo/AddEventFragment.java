package com.example.heliao.projgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heliao.projgo.projgoServerData.Conference;
import com.example.heliao.projgo.projgoServerData.Project;
import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.Task;
import com.example.heliao.projgo.projgoServerData.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class AddEventFragment extends Fragment {
    Button mdonebutton;
    TextView mEventlabel;
    EditText mEventName, mStartDate, mEndDate, mDescription, mPeople;
    Date addStartDate, addEndDate;
    String addEventName, addDescription, addPeople, tempStartDate, tempEndDate;
    String eventtype;
    String currentProjectName;
    SharedPreferences sharedPreferences;
    User currentUser;
    ServerDataManager dataManager;
    String currentUserName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_addevent_layout, container, false);
        //1. Reading values from fragment_addevent_layout
        mEventlabel = (TextView) rootview.findViewById(R.id.eventLabel_addevent);
        mEventName = (EditText) rootview.findViewById(R.id.eventname_edittext_addevent);
        mStartDate = (EditText) rootview.findViewById(R.id.startdate_editText_addevent);
        mEndDate = (EditText) rootview.findViewById(R.id.enddate_editText_addevent);
        mDescription = (EditText) rootview.findViewById(R.id.description_edittext_addevent);
        mPeople = (EditText) rootview.findViewById(R.id.people_edittext_addevent);

        //2.change label according the event type from **MainActivity**
        final Bundle bundle = this.getArguments();
        eventtype = bundle.getString("eventtype");
        mEventlabel.setText(eventtype);

        //get userinfo from sharedpreferences
        sharedPreferences = getActivity().getSharedPreferences("userinfo", 0);
        currentUserName = sharedPreferences.getString("nameKey", "missing");

        //3.Depend on which "Add Event" user choose, different object will be created
        dataManager = ServerDataManager.getInstance();
        currentUser = dataManager.userList.get(sharedPreferences.getString("nameKey", ""));

        final Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        mdonebutton = (Button) rootview.findViewById(R.id.buttonDone_addevent);
        mdonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventName = mEventName.getText().toString();
                addDescription = mDescription.getText().toString();
                addPeople = mPeople.getText().toString();
                //start date and end date
                tempStartDate = mStartDate.getText().toString();
                tempEndDate = mEndDate.getText().toString();

                //Step 3 continue******
                switch (eventtype) {
                    case "Project":
                        //add project
                        Project newproject = new Project(currentUser, addEventName, addDescription, tempStartDate, tempEndDate);
                        String[] participants = addPeople.split(",");
                        for(String name: participants){
                            newproject.participant.put(name,dataManager.userList.get(name));
                        }
                        //update current user
                        currentUser.project.put(addEventName, newproject);
                        //currentUser.add_project(newproject);
                        //update ServerDataManager
                        dataManager.addProject(addEventName, newproject);

                        break;
                    case "Task":
                        currentProjectName = bundle.getString("projectname");
                        Project selectedProject = dataManager.projectList.get(currentProjectName);
                        //add task
                        Task newtask = new Task(selectedProject, addEventName, addDescription, tempStartDate, tempEndDate);
                        String[] taskParticipants = addPeople.split(",");
                        for(String name: taskParticipants){
                            newtask.participant.put(name,dataManager.userList.get(name));
                        }
                        //update project
                        selectedProject.task.put(addEventName, newtask);
                        //update ServerDataManager
                        dataManager.addTask(addEventName, newtask);
                        break;
                    case "Conference":
                        //add conference
                        Conference newconference = new Conference(currentUser, addEventName, addDescription, tempStartDate, tempEndDate);

                        String[] conParticipants = addPeople.split(",");
                        for(String name: conParticipants){
                            newconference.participant.put(name,dataManager.userList.get(name));
                        }
                        //update user
                        currentUser.conference.put(addEventName, newconference);
                        //update ServerDataManager
                        dataManager.addConference(addEventName, newconference);
                        break;

                }

                startActivity(i);
            }
        });
        return rootview;
    }
}
