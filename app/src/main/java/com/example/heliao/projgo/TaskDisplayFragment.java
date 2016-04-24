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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
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
    Client mClient = new Client();
    User mCurrentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        currentuser = sharedPreferences.getString("nameKey", "miss");
        mCurrentUser = dataManager.userList.get(currentuser);

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

//        eventname = dataManager.eventList.get(eventname);
        selectedTask = dataManager.taskList.get(eventname);
        taskName.setText(selectedTask.name);
        taskDescription.setText(selectedTask.description);
        // List<String> participant = new ArrayList<String>();
        String taskParticipant = "";
        for(HashMap.Entry<String,User> entry : selectedTask.participant.entrySet()){
            taskParticipant +=entry.getValue().userId +" ";
        }


        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        String start = sdf1.format(selectedTask.start_time);
        String end = sdf1.format(selectedTask.end_time);

        taskPeople.setText(taskParticipant);
        taskStartDate.setText(start);
        taskEndDate.setText(end);

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
                    //new ServerModifyTask().execute(selectedTask);
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
                    new ServerDeleteTask().execute(selectedTask);
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(i);
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
                    new ServerUpdateProgress().execute(selectedTask);

                }
            });
            deleteButton.setText("QUIT");
            /**DELETE BUTTON*/
            /**DELETE BUTTON*/
            /**DELETE BUTTON*/
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ServerModifyTask().execute(selectedTask);
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

    private class ServerModifyTask extends AsyncTask<Task, String, Void> {

        @Override
        protected Void doInBackground(Task... params) {
            mClient.mod_task(mCurrentUser, params[0]);
            publishProgress("Modify Success");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(),values[0],Toast.LENGTH_LONG).show();
        }

    }

    private class ServerDeleteTask extends AsyncTask<Task, String, Void> {

        @Override
        protected Void doInBackground(Task... params) {
            mClient.del_task(mCurrentUser, params[0]);
            publishProgress("Delete Success");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(),values[0],Toast.LENGTH_LONG).show();
        }
    }

    private class ServerUpdateProgress extends AsyncTask<Task, String, Void> {

        @Override
        protected Void doInBackground(Task... params) {
            mClient.up_prog(mCurrentUser, params[0]);
            publishProgress("Update Success");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity().getApplicationContext(),values[0],Toast.LENGTH_LONG).show();
        }
    }
}
