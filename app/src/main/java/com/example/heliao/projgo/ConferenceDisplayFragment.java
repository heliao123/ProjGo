package com.example.heliao.projgo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
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
    User mCurrentUser;
    AddConferenceFragment conferenceFragment;
    FragmentManager conferenceFragmentManager;
    Client mClient = new Client();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        currentuser = sharedPreferences.getString("nameKey", "miss");
        mCurrentUser = dataManager.userList.get(currentuser);

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

        //eventname = dataManager.eventList.get(eventname);
        selectedConference = dataManager.conferenceList.get(eventname);
        conferenceName.setText(selectedConference.name);
        conferenceDescription.setText(selectedConference.description);
        // List<String> participant = new ArrayList<String>();
        String conParticipant = "";
        for(HashMap.Entry<String,User> entry : selectedConference.participant.entrySet()){
            conParticipant +=entry.getValue().userId +" ";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");

        String condate = sdf1.format(selectedConference.start_time);
        String start = sdf2.format(selectedConference.start_time);
        String end = sdf2.format(selectedConference.end_time);


        conferencePeople.setText(conParticipant);
        conferenceStartTime.setText(start);
        conferenceEndTime.setText(end);
        conferencetDate.setText(condate);


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
                    new ServerDeleteConference().execute(selectedConference);
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(i);
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
                   new ServerModifyConference().execute(selectedConference);
                    Toast.makeText(getActivity().getApplicationContext(), "participant " + currentuser + " is removed from project" + selectedConference.name, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(i);
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


    private class ServerDeleteConference extends AsyncTask<Conference, String, Void> {

        @Override
        protected Void doInBackground(Conference... params) {

            mClient.del_conf(mCurrentUser, params[0]);
            publishProgress("Delete Success");
            return null;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(),values[0],Toast.LENGTH_LONG).show();
        }
    }

    private class ServerModifyConference extends AsyncTask<Conference, String, Void> {

        @Override
        protected Void doInBackground(Conference... params) {

            mClient.mod_conf(mCurrentUser, params[0]);
            publishProgress("Modify Success");
            return null;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(),values[0],Toast.LENGTH_LONG).show();
        }
    }


}
