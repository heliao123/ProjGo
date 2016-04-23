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
 * Created by HeLiao on 4/14/2016.
 */
public class AddConferenceFragment extends Fragment {
    Button mdonebutton;
    TextView mEventlabel, mAddParticipantButton, mPeopleDsiplay, mRemoveParticipantButton;
    EditText mEventName, mStartTime, mEndTime, mDescription, mPeople, mConferDate;
    Date addStartDate, addEndDate;
    String addEventName, addDescription, addPeople, tempStartTime, tempEndTime, conDate;
    String eventtype;
    String currentProjectName;
    SharedPreferences sharedPreferences;
    User currentUser;
    ServerDataManager dataManager;
    String currentUserName;
    String modifyconference;
    Client mClient = new Client();
    SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
    SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mClient = new Client();
        View rootview = inflater.inflate(R.layout.fragment_meeting, container, false);
        //1. Reading values from fragment_addevent_layout
        mEventlabel = (TextView) rootview.findViewById(R.id.meeting_Label_addevent);
        mEventName = (EditText) rootview.findViewById(R.id.meeting_name_edittext_addevent);
        mStartTime = (EditText) rootview.findViewById(R.id.meeting_starttime_editText_addevent);
        mEndTime = (EditText) rootview.findViewById(R.id.meeting_endtime_editText_addevent);
        mDescription = (EditText) rootview.findViewById(R.id.meeting_description_edittext_addevent);
        mPeople = (EditText) rootview.findViewById(R.id.meeting_people_edittext_addevent);
        mConferDate = (EditText) rootview.findViewById(R.id.meeting_edittext_Date);

        mAddParticipantButton = (TextView) rootview.findViewById(R.id.meeting_addParticipantButton);
        mPeopleDsiplay = (TextView) rootview.findViewById(R.id.meeting_people_textview_addParticipant);
        mRemoveParticipantButton = (TextView) rootview.findViewById(R.id.meeting_removeparticipant);

        mAddParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeopleDsiplay.append(mPeople.getText().toString() + ",");
                mPeople.setText("");
            }
        });

        mRemoveParticipantButton.setOnClickListener(new View.OnClickListener() {
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


        DateTimePickDialog datepickDialogConDate = new DateTimePickDialog(getActivity().getWindow().getContext(), mConferDate);
        datepickDialogConDate.showDatePickDialogOnClick();

        DateTimePickDialog timepickDialogStartTime = new DateTimePickDialog(getActivity().getWindow().getContext(), mStartTime);
        timepickDialogStartTime.showTimePickDialogOnClick();

        DateTimePickDialog timepickDialogEndTime = new DateTimePickDialog(getActivity().getWindow().getContext(), mEndTime);
        timepickDialogEndTime.showTimePickDialogOnClick();


        //2.change label according the event type from **MainActivity**
        final Bundle bundle = this.getArguments();
        eventtype = bundle.getString("eventtype");
        modifyconference = bundle.getString("modifyconference");
        modifyconference = dataManager.eventList.get(modifyconference);
        mEventlabel.setText(eventtype);

        if (modifyconference != null && eventtype == "Conference") {
            /**
             * MODIFY Conference
             * MODIFY Conference
             * MODIFY Conference
             */

            Conference modifyVersion = dataManager.conferenceList.get(modifyconference);
            mEventName.setText(modifyVersion.name);
            mDescription.setText(modifyVersion.description);

            String condate = sdf1.format(modifyVersion.start_time);
            String start = sdf2.format(modifyVersion.start_time);
            String end = sdf2.format(modifyVersion.end_time);

            mConferDate.setText(condate);
            String par = "";
            for (Map.Entry<String, User> entry : modifyVersion.participant.entrySet()) {
                par = entry.getValue().userId + ",";
            }
            mPeopleDsiplay.setText(par);
            //start date and end date
            mStartTime.setText(start);
            mEndTime.setText(end);
        }

        //get userinfo from sharedpreferences
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        currentUserName = sharedPreferences.getString("nameKey", "missing");

        //3.Depend on which "Add Event" user choose, different object will be created
        dataManager = ServerDataManager.getInstance();
        currentUser = dataManager.userList.get(sharedPreferences.getString("nameKey", ""));

        final Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        mdonebutton = (Button) rootview.findViewById(R.id.meeting_buttonDone_addevent);
        mdonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventName = mEventName.getText().toString();
                addDescription = mDescription.getText().toString();
                addPeople = mPeopleDsiplay.getText().toString();
                //start date and end date
                tempStartTime = mStartTime.getText().toString();
                tempEndTime = mEndTime.getText().toString();
                conDate = mConferDate.getText().toString();
                //add conference
                Conference newconference = null;
                if (modifyconference != null) {
                    newconference = dataManager.conferenceList.get(modifyconference);

                    try {
                        String newStartTime = conDate + " "+tempStartTime;
                        String newEndTime = conDate + " " + tempEndTime;
                        newconference.start_time = sdf3.parse(newStartTime);
                        newconference.end_time = sdf3.parse(newEndTime);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    newconference.name = addEventName;
                    String[] conParticipants = addPeople.split(",");
                    for (String name : conParticipants) {
                        newconference.participant_list.add(name);
                    }
                    newconference.description = addDescription;

                } else {
                    newconference = new Conference(currentUser, addEventName, addDescription, tempStartTime, tempEndTime, conDate);

                    String[] conParticipants = addPeople.split(",");
                    for (String name : conParticipants) {
                        newconference.participant_list.add(name);
                    }
                }
                //update user
                currentUser.conference.put(addEventName, newconference);
                //update ServerDataManager
                dataManager.addConference(addEventName, newconference);
                /**
                 *
                 * ADD  to the Server
                 * ADD  to the Server
                 * ADD  to the Server
                 * ADD  to the Server
                 */
                if (modifyconference != null && eventtype == "Conference") {
                    new ServerModifyConference().execute(newconference);
                } else {
                    new ServerAddConference().execute(newconference);
                }

                startActivity(i);

            }
        });


        return rootview;
    }

    private class ServerAddConference extends AsyncTask<Conference, Void, Void> {

        @Override
        protected Void doInBackground(Conference... params) {

            mClient.new_conf(currentUser, params[0]);
            return null;

        }
    }

    private class ServerModifyConference extends AsyncTask<Conference, Void, Void> {

        @Override
        protected Void doInBackground(Conference... params) {

            mClient.mod_conf(currentUser, params[0]);
            return null;

        }
    }

}
