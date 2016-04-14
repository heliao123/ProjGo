package com.example.heliao.projgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heliao.projgo.projgoServerData.Conference;
import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.User;

import java.util.Date;

/**
 * Created by HeLiao on 4/14/2016.
 */
public class AddConferenceFragment extends Fragment {
    Button mdonebutton;
    TextView mEventlabel, mAddParticipantButton, mPeopleDsiplay;
    EditText mEventName, mStartTime, mEndTime, mDescription, mPeople, mConferDate;
    Date addStartDate, addEndDate;
    String addEventName, addDescription, addPeople, tempStartTime, tempEndTime, conDate;
    String eventtype;
    String currentProjectName;
    SharedPreferences sharedPreferences;
    User currentUser;
    ServerDataManager dataManager;
    String currentUserName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        mAddParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeopleDsiplay.append(mPeople.getText().toString() + ",");
                mPeople.setText("");
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
        mEventlabel.setText(eventtype);

        //get userinfo from sharedpreferences
        sharedPreferences = getActivity().getSharedPreferences("userinfo", 0);
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
                Conference newconference = new Conference(currentUser, addEventName, addDescription, tempStartTime, tempEndTime,conDate);

                String[] conParticipants = addPeople.split(",");
                for (String name : conParticipants) {
                    newconference.participant.put(name, name);
                }
                //update user
                currentUser.conference.put(addEventName, newconference);
                //update ServerDataManager
                dataManager.addConference(addEventName, newconference);
                startActivity(i);

            }
        });


        return rootview;
    }
}
