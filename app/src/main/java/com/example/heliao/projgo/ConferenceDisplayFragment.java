package com.example.heliao.projgo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.heliao.projgo.projgoServerData.ServerDataManager;

/**
 * Created by HeLiao on 4/14/2016.
 */
public class ConferenceDisplayFragment extends Fragment {

    TextView conferenceName, conferenceDescription,conferencePeople,conferencetDate,conferenceStartTime,conferenceEndTime;
    Button donebutton;
    ServerDataManager dataManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = (View) inflater.inflate(R.layout.fragment_conference_display, container, false);
        eventName = (TextView) rootview.findViewById(R.id.eventname_edittext_displayevent);
        eventLabel = (TextView) rootview.findViewById(R.id.eventLabel_displayevent);
        eventDescription = (TextView) rootview.findViewById(R.id.description_edittext_displayevent);
        eventPeople = (TextView) rootview.findViewById(R.id.people_edittext_displayevent);
        eventStartDate = (TextView) rootview.findViewById(R.id.startdate_edittext_displayevent);
        eventEndDate = (TextView) rootview.findViewById(R.id.enddate_editText__displayevent);
        donebutton = (Button) rootview.findViewById(R.id.buttonDone_displayevent);
        //final Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);

        Bundle bundle = this.getArguments();
        String eventType = bundle.getString("eventtype");
        eventLabel.setText(eventType);
        String eventname = bundle.getString("eventname");
        dataManager = ServerDataManager.getInstance();


        return rootview;
    }


}
