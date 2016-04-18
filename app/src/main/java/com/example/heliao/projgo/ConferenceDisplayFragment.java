package com.example.heliao.projgo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heliao.projgo.projgoServerData.Conference;
import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.User;

import java.util.HashMap;

/**
 * Created by HeLiao on 4/14/2016.
 */
public class ConferenceDisplayFragment extends Fragment {

    TextView conferenceName, conferenceDescription,conferencePeople,conferencetDate,conferenceStartTime,conferenceEndTime;
    Button donebutton,modifyButton,deleteButton;
    ServerDataManager dataManager;
    Conference selectedConference;
    String currentuser;
    AddConferenceFragment conferenceFragment;
    FragmentManager conferenceFragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        currentuser = sharedPreferences.getString("nameKey", "miss");

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

        final Bundle bundle = this.getArguments();
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
        conferenceStartTime.setText(selectedConference.start_time_string);
        conferenceEndTime.setText(selectedConference.end_time_string);
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


        if (isProjectOwner(selectedConference.holder)) {
            /**MODIFY BUTTON*/
            /**MODIFY BUTTON*/
            /**MODIFY BUTTON*/
            modifyButton.setVisibility(View.VISIBLE);
            modifyButton.setText("MODIFY");
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putString("modifyconference", selectedConference.name);
                    bundle.putString("eventtype","Conference");
                    conferenceFragment = new AddConferenceFragment();
                    conferenceFragment.setArguments(bundle);
                    conferenceFragmentManager = getFragmentManager();
                    FragmentTransaction tft = conferenceFragmentManager.beginTransaction();
                    tft.replace(R.id.content_frame, conferenceFragment);
                    tft.commit();
                }
            });
            /**DELETE BUTTON*/
            /**DELETE BUTTON*/
            /**DELETE BUTTON*/
            deleteButton.setText("DELETE");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /******
                     *
                     *
                     *
                     * Delete project on the server
                     *
                     *
                     *
                     *
                     *
                     *
                     */
                }
            });
        } else {
            modifyButton.setVisibility(View.INVISIBLE);
            deleteButton.setText("QUIT");
            /**DELETE BUTTON*/
            /**DELETE BUTTON*/
            /**DELETE BUTTON*/
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedConference.participant.remove(currentuser);
                    Toast.makeText(getActivity().getApplicationContext(), "participant " + currentuser + " is removed from project" + selectedConference.name, Toast.LENGTH_LONG).show();
                }
            });
        }

        return rootview;
    }

    public boolean isProjectOwner(User projectHolder) {
        if (projectHolder.userId.equals(currentuser)) {
            return true;
        } else {
            return false;
        }

    }


}
