package com.example.heliao.projgo;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class AddEventFragment extends Fragment {
    Button mdonebutton;
    TextView mEventlabel, mAddParticipantButton, mPeopleDsiplay, mRemoveParticipant;
    EditText mEventName, mStartDate, mEndDate, mDescription, mPeople;
    Date addStartDate, addEndDate;
    String addEventName, addDescription, addPeople, tempStartDate, tempEndDate;
    String eventtype;
    String currentProjectName;
    SharedPreferences sharedPreferences;
    User currentUser;
    ServerDataManager dataManager;
    String currentUserName;
    String modifyProject, modifyTask;
    Client mClient = new Client();
    SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mClient = new Client();
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
        mRemoveParticipant = (TextView) rootview.findViewById(R.id.removeParticipant_addevent);

        mAddParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeopleDsiplay.append(mPeople.getText().toString() + ",");
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
                        people = people.substring(0, people.lastIndexOf(",") + 1);
                        mPeopleDsiplay.setText(people);
                    }
                } else {
                    mPeopleDsiplay.setText("");
                }
            }
        });


        DateTimePickDialog datepickDialogStartDate = new DateTimePickDialog(getActivity().getWindow().getContext(), mStartDate);
        datepickDialogStartDate.showDatePickDialogOnClick();

        DateTimePickDialog datepickDialogEndDate = new DateTimePickDialog(getActivity().getWindow().getContext(), mEndDate);
        datepickDialogEndDate.showDatePickDialogOnClick();


        //2.change label according the event type from **MainActivity**
        final Bundle bundle = this.getArguments();
        eventtype = bundle.getString("eventtype");
        modifyProject = bundle.getString("modifyproject");
        modifyProject = dataManager.eventList.get(modifyProject);
        modifyTask = bundle.getString("modifytask");
        modifyTask = dataManager.eventList.get(modifyTask);
        mEventlabel.setText(eventtype);

        //Check if project needs to be modifyed
        if (modifyProject != null && eventtype == "Project") {

            Project modifyVersion = dataManager.projectList.get(modifyProject);
            mEventName.setText(modifyVersion.name);
            mDescription.setText(modifyVersion.description);
            String par = "";
            for (Map.Entry<String, User> entry : modifyVersion.participant.entrySet()) {
                par = entry.getValue().userId + ",";
            }
            mPeopleDsiplay.setText(par);
            //start date and end date
            String start = sdf1.format(modifyVersion.start_time);
            String end = sdf1.format(modifyVersion.end_time);
            mStartDate.setText(start);
            mEndDate.setText(end);
        } else if (modifyTask != null && eventtype == "Task") {

            Task modifyVersion = dataManager.taskList.get(modifyTask);

            mEventName.setText(modifyVersion.name);
            mDescription.setText(modifyVersion.description);
            String par = "";
            for (Map.Entry<String, User> entry : modifyVersion.participant.entrySet()) {
                par = entry.getValue().userId + ",";
            }
            mPeopleDsiplay.setText(par);
            //start date and end date
            String start = sdf1.format(modifyVersion.start_time);
            String end = sdf1.format(modifyVersion.end_time);
            mStartDate.setText(start);
            mEndDate.setText(end);
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
                        Project newproject = null;
                        if (modifyProject != null) {
                            newproject = dataManager.projectList.get(modifyProject);
                            try {
                                newproject.start_time = sdf1.parse(tempStartDate);
                                newproject.end_time = sdf1.parse(tempEndDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            newproject.name = addEventName;
                            String[] conParticipants = addPeople.split(",");
                            for (String name : conParticipants) {
                                newproject.participant_list.add(name);
                            }
                            newproject.description = addDescription;

                        } else {
                            newproject = new Project(currentUser, addEventName, addDescription, tempStartDate, tempEndDate);

                            String[] conParticipants = addPeople.split(",");
                            for (String name : conParticipants) {
                                newproject.participant_list.add(name);
                            }
                        }

                        //update current user
                        currentUser.project.put(addEventName, newproject);

                        //update ServerDataManager
                        dataManager.addProject(addEventName, newproject);
                        if (modifyProject != null && eventtype == "Project") {
                            new ServerModifyProject().execute(newproject);
                        } else {
                            new ServerAddProject().execute(newproject);
                        }


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
                        currentProjectName = dataManager.eventList.get(currentProjectName);
                        Project selectedProject = dataManager.projectList.get(currentProjectName);
                        //add task
                        Task newtask = null;
                        if (modifyTask != null) {
                            newtask = dataManager.taskList.get(modifyTask);
                            try {
                                newtask.start_time = sdf1.parse(tempStartDate);
                                newtask.end_time = sdf1.parse(tempEndDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            newtask.name = addEventName;
                            String[] conParticipants = addPeople.split(",");
                            for (String name : conParticipants) {
                                newtask.participant_list.add(name);
                            }
                            newtask.description = addDescription;

                        } else {
                            newtask = new Task(selectedProject, addEventName, addDescription, tempStartDate, tempEndDate);

                            String[] conParticipants = addPeople.split(",");
                            for (String name : conParticipants) {
                                newtask.participant_list.add(name);
                            }
                        }

                        //update project
                        // selectedProject.task.put(addEventName, newtask);
                        //update ServerDataManager
                        dataManager.addTask(addEventName, newtask);

                        if (modifyTask != null && eventtype == "Task") {
                            new ServerModifyTask().execute(newtask);
                        } else {
                            new ServerAddTask().execute(newtask);
                        }


                        break;

                }

                startActivity(i);
            }
        });
        return rootview;
    }


    private class ServerAddProject extends AsyncTask<Project, String, Void> {

        @Override
        protected Void doInBackground(Project... params) {
            mClient.new_proj(currentUser, params[0]);

            return null;
        }
    }

    private class ServerModifyProject extends AsyncTask<Project, String, Void> {

        @Override
        protected Void doInBackground(Project... params) {
            mClient.mod_proj(currentUser, params[0]);
            return null;
        }
    }

    private class ServerDeleteProject extends AsyncTask<Project, String, Void> {

        @Override
        protected Void doInBackground(Project... params) {
            mClient.del_proj(currentUser, params[0]);
            publishProgress("Delete Success");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(), values[0], Toast.LENGTH_LONG).show();
        }
    }

    private class ServerAddTask extends AsyncTask<Task, String, Void> {

        @Override
        protected Void doInBackground(Task... params) {
            mClient.new_task(currentUser, params[0]);
            publishProgress("Add Success");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(), values[0], Toast.LENGTH_LONG).show();
        }

    }

    private class ServerModifyTask extends AsyncTask<Task, String, Void> {

        @Override
        protected Void doInBackground(Task... params) {
            mClient.mod_task(currentUser, params[0]);
            publishProgress("Modify Success");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(), values[0], Toast.LENGTH_LONG).show();
        }

    }

    private class ServerDeleteTask extends AsyncTask<Task, String, Void> {

        @Override
        protected Void doInBackground(Task... params) {
            mClient.del_task(currentUser, params[0]);
            publishProgress("Delete Success");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(), values[0], Toast.LENGTH_LONG).show();
        }
    }


}
