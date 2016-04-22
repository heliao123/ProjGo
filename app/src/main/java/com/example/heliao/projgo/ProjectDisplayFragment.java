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

import java.util.HashMap;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class ProjectDisplayFragment extends Fragment {
    TextView eventName, eventLabel, eventDescription, eventPeople, eventStartDate, eventEndDate;
    Button donebutton, modifyButton, deleteButton;
    ServerDataManager dataManager;
    Project selectedProject;

    AddEventFragment projectFragment;
    FragmentManager projectFragmentManager;
    //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    String currentuser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        currentuser = sharedPreferences.getString("nameKey", "miss");


        View rootview = (View) inflater.inflate(R.layout.fragment_project_display, container, false);
        eventName = (TextView) rootview.findViewById(R.id.eventname_edittext_displayevent);
        eventLabel = (TextView) rootview.findViewById(R.id.eventLabel_displayevent);
        eventDescription = (TextView) rootview.findViewById(R.id.description_edittext_displayevent);
        eventPeople = (TextView) rootview.findViewById(R.id.people_edittext_displayevent);
        eventStartDate = (TextView) rootview.findViewById(R.id.startdate_edittext_displayevent);
        eventEndDate = (TextView) rootview.findViewById(R.id.enddate_editText__displayevent);


        donebutton = (Button) rootview.findViewById(R.id.buttonDone_displayevent);
        modifyButton = (Button) rootview.findViewById(R.id.modify_projectdisplay);
        deleteButton = (Button) rootview.findViewById(R.id.delete_projectdisplay);
        //final Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);

        final Bundle bundle = this.getArguments();
        String eventType = bundle.getString("eventtype");
        eventLabel.setText(eventType);
        String eventname = bundle.getString("eventname");
        dataManager = ServerDataManager.getInstance();

        selectedProject = dataManager.projectList.get(eventname);
        eventName.setText(selectedProject.name);
        eventDescription.setText(selectedProject.description);
        // List<String> participant = new ArrayList<String>();
        String participant = "";
        for (HashMap.Entry<String, String> entry : selectedProject.participant.entrySet()) {
            participant += entry.getValue() + " ";
        }
        eventPeople.setText(participant);
        eventStartDate.setText(selectedProject.startdate);
        eventEndDate.setText(selectedProject.enddate);

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

        if (isProjectOwner(selectedProject.holder)) {
            /**MODIFY BUTTON*/
            /**MODIFY BUTTON*/
            /**MODIFY BUTTON*/
            modifyButton.setVisibility(View.VISIBLE);
            modifyButton.setText("MODIFY");
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putString("modifyproject", selectedProject.name);
                    bundle.putString("eventtype","Project");
                    projectFragment = new AddEventFragment();
                    projectFragment.setArguments(bundle);
                    projectFragmentManager = getFragmentManager();
                    FragmentTransaction pft = projectFragmentManager.beginTransaction();
                    pft.replace(R.id.content_frame, projectFragment);
                    pft.commit();
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
                        selectedProject.participant.remove(currentuser);
                        Toast.makeText(getActivity().getApplicationContext(),"participant "+currentuser+" is removed from project" + selectedProject.name,Toast.LENGTH_LONG).show();
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
