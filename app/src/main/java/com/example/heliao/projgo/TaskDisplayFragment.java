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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heliao.projgo.projgoServerData.Client;
import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.Task;
import com.example.heliao.projgo.projgoServerData.User;

import java.util.HashMap;

/**
 * Created by HeLiao on 4/14/2016.
 */
public class TaskDisplayFragment extends Fragment {
    TextView taskName, taskDescription,taskPeople,taskStartDate,taskEndDate,taskProjectName;
    EditText taskPrograss;
    Button donebutton,modifyButton,deleteButton;
    ServerDataManager dataManager;
    Task selectedTask;
    String currentuser;
    AddEventFragment taskFragment;
    FragmentManager taskFragmentManager;
    Client mClient;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        currentuser = sharedPreferences.getString("nameKey", "miss");

        View rootview = (View) inflater.inflate(R.layout.fragment_task_display, container, false);
        taskName = (TextView) rootview.findViewById(R.id.taskname_edittext_displaytask);
        taskProjectName = (TextView) rootview.findViewById(R.id.projectname_displaytask);
        taskDescription = (TextView) rootview.findViewById(R.id.description_edittext_displaytask);
        taskPeople = (TextView) rootview.findViewById(R.id.people_edittext_displaytask);
        taskStartDate = (TextView) rootview.findViewById(R.id.startdate_edittext_displaytask);
        taskEndDate = (TextView) rootview.findViewById(R.id.enddate_editText__displaytask);
        donebutton = (Button) rootview.findViewById(R.id.buttonDone_displaytask);
        taskPrograss=(EditText)rootview.findViewById(R.id.progess_displaytask);
        //final Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);

        modifyButton = (Button) rootview.findViewById(R.id.modify_displaytask);
        deleteButton = (Button) rootview.findViewById(R.id.delete_displaytask);

        final Bundle bundle = this.getArguments();
        String eventType = bundle.getString("eventtype");
        String eventname = bundle.getString("eventname");
        dataManager = ServerDataManager.getInstance();

        selectedTask = dataManager.taskList.get(eventname);
        taskName.setText(selectedTask.name);
        taskDescription.setText(selectedTask.description);
        // List<String> participant = new ArrayList<String>();
        String taskParticipant = "";
        for(HashMap.Entry<String,String> entry : selectedTask.participant.entrySet()){
            taskParticipant +=entry.getValue() +" ";
        }
        taskPeople.setText(taskParticipant);
        taskStartDate.setText(selectedTask.start_time_string);
        taskEndDate.setText(selectedTask.end_time_string);

        taskProjectName.setText(selectedTask.project.name);
        taskPrograss.setText(selectedTask.progress);



        final MainFragment mainFragment = new MainFragment();


        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(i);
                String progress = taskPrograss.getText().toString();
                selectedTask.progress = progress;

                FragmentManager fragmentManager_mainfragment = getFragmentManager();
                FragmentTransaction fragmentTransaction_mainfragment = fragmentManager_mainfragment.beginTransaction();
                fragmentTransaction_mainfragment.replace(R.id.content_frame, mainFragment);
                fragmentTransaction_mainfragment.commit();

            }
        });

        if (isProjectOwner(selectedTask.project.holder)) {
            /**MODIFY BUTTON*/
            /**MODIFY BUTTON*/
            /**MODIFY BUTTON*/
            modifyButton.setVisibility(View.VISIBLE);
            modifyButton.setText("MODIFY");
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putString("modifytask", selectedTask.name);
                    bundle.putString("eventtype","Task");
                    bundle.putString("projectname",selectedTask.project.name);
                    taskFragment = new AddEventFragment();
                    taskFragment.setArguments(bundle);
                    taskFragmentManager = getFragmentManager();
                    FragmentTransaction tft = taskFragmentManager.beginTransaction();
                    tft.replace(R.id.content_frame, taskFragment);
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
            modifyButton.setVisibility(View.VISIBLE);
            modifyButton.setText("UPDATE");
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String progress = taskPrograss.getText().toString();
                    selectedTask.progress = progress;
                }
            });
            deleteButton.setText("QUIT");
            /**DELETE BUTTON*/
            /**DELETE BUTTON*/
            /**DELETE BUTTON*/
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTask.participant.remove(currentuser);
                    Toast.makeText(getActivity().getApplicationContext(), "participant " + currentuser + " is removed from project" + selectedTask.name, Toast.LENGTH_LONG).show();
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
