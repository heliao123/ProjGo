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
import android.widget.TextView;

import com.example.heliao.projgo.projgoServerData.Conference;
import com.example.heliao.projgo.projgoServerData.ServerDataManager;

import java.util.HashMap;

/**
 * Created by HeLiao on 4/14/2016.
 */
public class ConferenceDisplayFragment extends Fragment {

    TextView conferenceName, conferenceDescription,conferencePeople,conferencetDate,conferenceStartTime,conferenceEndTime;
    Button donebutton,modifyButton,deleteButton;
    ServerDataManager dataManager;
    Conference selectedConference;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = (View) inflater.inflate(R.layout.fragment_conference_display, container, false);
        conferenceName = (TextView) rootview.findViewById(R.id.taskname_edittext_displayconference);
        conferencetDate = (TextView) rootview.findViewById(R.id.conDate_displayconference);
        conferenceStartTime = (TextView) rootview.findViewById(R.id.starttime_edittext_displayconference);
        conferenceEndTime = (TextView) rootview.findViewById(R.id.endtime_editText__displayconference);
        conferenceDescription = (TextView) rootview.findViewById(R.id.description_edittext_displayconference);
        conferencePeople = (TextView) rootview.findViewById(R.id.people_edittext_displayconference);


        donebutton = (Button) rootview.findViewById(R.id.buttonDone_displayconference);
        modifyButton = (Button) rootview.findViewById(R.id.modify_displayconference);
        deleteButton = (Button) rootview.findViewById(R.id.delete_displayconference);
        //final Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);

        Bundle bundle = this.getArguments();
        String eventType = bundle.getString("eventtype");
        String eventname = bundle.getString("eventname");
        dataManager = ServerDataManager.getInstance();

        selectedConference = dataManager.conferenceList.get(eventname);
        conferenceName.setText(selectedConference.name);
        conferenceDescription.setText(selectedConference.description);
        // List<String> participant = new ArrayList<String>();
        String conParticipant = "";
        for(HashMap.Entry<String,String> entry : selectedConference.participant.entrySet()){
            conParticipant +=entry.getValue() +" ";
        }
        conferencePeople.setText(conParticipant);
        conferenceStartTime.setText(selectedConference.start_time);
        conferenceEndTime.setText(selectedConference.end_time);
        conferencetDate.setText(selectedConference.conferencedate);


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
