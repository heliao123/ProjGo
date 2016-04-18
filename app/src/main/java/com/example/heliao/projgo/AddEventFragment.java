package com.example.heliao.projgo;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heliao.projgo.projgoServerData.Project;
import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.Task;
import com.example.heliao.projgo.projgoServerData.User;

import java.util.Date;
import java.util.Map;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class AddEventFragment extends Fragment {
    Button mdonebutton;
    TextView mEventlabel,mAddParticipantButton,mPeopleDsiplay,mRemoveParticipant;
    EditText mEventName, mStartDate, mEndDate, mDescription, mPeople;
    Date addStartDate, addEndDate;
    String addEventName, addDescription, addPeople, tempStartDate, tempEndDate;
    String eventtype;
    String currentProjectName;
    SharedPreferences sharedPreferences;
    User currentUser;
    ServerDataManager dataManager;
    String currentUserName;
    String modifyProject,modifyTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_addevent_layout, container, false);
        //1. Reading values from fragment_addevent_layout
        mEventlabel = (TextView) rootview.findViewById(R.id.con_Label_addevent);
        mEventName = (EditText) rootview.findViewById(R.id.con_name_edittext_addevent);
        mStartDate = (EditText) rootview.findViewById(R.id.con_starttime_editText_addevent);
        mEndDate = (EditText) rootview.findViewById(R.id.con_enddate_editText_addevent);
        mDescription = (EditText) rootview.findViewById(R.id.con_description_edittext_addevent);
        mPeople = (EditText) rootview.findViewById(R.id.con_people_edittext_addevent);

        mAddParticipantButton = (TextView) rootview.findViewById(R.id.addParticipantButton);
        mPeopleDsiplay = (TextView) rootview.findViewById(R.id.con_people_textview_addParticipant);
        mRemoveParticipant =(TextView) rootview.findViewById(R.id.removeParticipant_addevent);

        mAddParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeopleDsiplay.append(mPeople.getText().toString()+",");
                mPeople.setText("");
            }
        });

        mRemoveParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String people = mPeopleDsiplay.getText().toString();
                if (people == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "No one is added yet", Toast.LENGTH_LONG).show();
                }
                if (people.lastIndexOf(",") != -1) {
                    people = people.substring(0, people.lastIndexOf(","));
                    if (people.lastIndexOf(",") == -1) {
                        mPeopleDsiplay.setText("");
                    } else {
                        people = people.substring(0, people.lastIndexOf(",")+1);
                        mPeopleDsiplay.setText(people);
                    }
                } else {
                    mPeopleDsiplay.setText("");
                }
            }
        });


        DateTimePickDialog datepickDialogStartDate = new DateTimePickDialog(getActivity().getWindow().getContext(),mStartDate);
        datepickDialogStartDate.showDatePickDialogOnClick();

        DateTimePickDialog datepickDialogEndDate = new DateTimePickDialog(getActivity().getWindow().getContext(),mEndDate);
        datepickDialogEndDate.showDatePickDialogOnClick();


        //2.change label according the event type from **MainActivity**
        final Bundle bundle = this.getArguments();
        eventtype = bundle.getString("eventtype");
        modifyProject = bundle.getString("modifyproject");
        modifyTask = bundle.getString("modifytask");
        mEventlabel.setText(eventtype);

        //Check if project needs to be modifyed
        if(modifyProject!=null&&eventtype=="Project"){

            Project modifyVersion = dataManager.projectList.get(modifyProject);
            mEventName.setText(modifyVersion.name);
            mDescription.setText(modifyVersion.description);
            String par="";
            for(Map.Entry<String,String> entry :modifyVersion.participant.entrySet()){
                par = entry.getValue() +",";
            }
            mPeopleDsiplay.setText(par);
            //start date and end date
            mStartDate.setText(modifyVersion.startdate);
            mEndDate.setText(modifyVersion.enddate);
        }else if(modifyTask!=null && eventtype=="Task"){

            Task modifyVersion = dataManager.taskList.get(modifyTask);
            mEventName.setText(modifyVersion.name);
            mDescription.setText(modifyVersion.description);
            String par="";
            for(Map.Entry<String,String> entry :modifyVersion.participant.entrySet()){
                par = entry.getValue() +",";
            }
            mPeopleDsiplay.setText(par);
            //start date and end date
            mStartDate.setText(modifyVersion.start_time_string);
            mEndDate.setText(modifyVersion.end_time_string);
        }

        //get userinfo from sharedpreferences
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        currentUserName = sharedPreferences.getString("nameKey", "missing");

        //3.Depend on which "Add Event" user choose, different object will be created
        dataManager = ServerDataManager.getInstance();
        currentUser = dataManager.userList.get(sharedPreferences.getString("nameKey", ""));

        final Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        mdonebutton = (Button) rootview.findViewById(R.id.con_buttonDone_addevent);
        mdonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventName = mEventName.getText().toString();
                addDescription = mDescription.getText().toString();
                addPeople = mPeopleDsiplay.getText().toString();
                //start date and end date
                tempStartDate = mStartDate.getText().toString();
                tempEndDate = mEndDate.getText().toString();

                //Step 3 continue******
                switch (eventtype) {
                    case "Project":
                        /***
                         *
                         * Save project and update server
                         * Save project and update server
                         * Save project and update server
                         * Save project and update server
                         *
                         */
                        //add project
                        Project newproject = new Project(currentUser, addEventName, addDescription, tempStartDate, tempEndDate);
                        String[] participants = addPeople.split(",");
                        for (String name : participants) {
                            newproject.participant.put(name, name);
                        }
                        //update current user
                        currentUser.project.put(addEventName, newproject);
                        //currentUser.add_project(newproject);
                        //update ServerDataManager
                        dataManager.addProject(addEventName, newproject);

                        break;
                    case "Task":
                        /***
                         *
                         * Save task and update server
                         * Save task and update server
                         * Save task and update server
                         * Save task and update server
                         *
                         */
                        currentProjectName = bundle.getString("projectname");
                        Project selectedProject = dataManager.projectList.get(currentProjectName);
                        //add task
                        Task newtask = new Task(selectedProject, addEventName, addDescription, tempStartDate, tempEndDate);
                        String[] taskParticipants = addPeople.split(",");
                        for (String name : taskParticipants) {
                            newtask.participant.put(name, name);
                        }
                        //update project
                       // selectedProject.task.put(addEventName, newtask);
                        //update ServerDataManager
                        dataManager.addTask(addEventName, newtask);
                        break;

                }

                startActivity(i);}
        });
        return rootview;
    }

}
