package com.example.heliao.projgo;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MainActivity extends AppCompatActivity implements FragmentConnector{

    CalendarFragment calendarfragment;
    TaskListFragment tasklistfragment;
    ProjectDisplayFragment taskDetailFragment;
    FragmentManager fragmentManager_main = getFragmentManager();
    FragmentTransaction fragmentTransaction_main = fragmentManager_main.beginTransaction();
    AddEventFragment addEventFragment = new AddEventFragment();
    ListView addlist;
    MainFragment mainFragment;
    SharedPreferences sharedPreferences;
    ServerDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainFragment = new MainFragment();
        /**
        //get bundles from user log in page and user registration page,
        //intent comes from **RegistrationActivity**
        Intent intentExtra = getIntent();
        Bundle foreignBundle = intentExtra.getExtras();

        try{
        if(!foreignBundle.isEmpty()){
            mainFragment.setArguments(foreignBundle);
        }else{
            Toast.makeText(getApplicationContext(), "Currently there is no user loged in", Toast.LENGTH_LONG).show();
        }}catch ( NullPointerException e){
            System.out.print(e.toString());
        }
*/
        fragmentTransaction_main.add(R.id.content_frame, mainFragment);
        fragmentTransaction_main.commit();

    }

    // this gets called by the calendar fragment when the user clicks the date
    @Override
    public void getValueFromFragmentUsingInterface(int year, int month, int day) {
        tasklistfragment = (TaskListFragment) getFragmentManager().findFragmentById(R.id.listview_fragmentcontainer);
        tasklistfragment.updateinfo(year,month,day);
    }


    @Override
    public void getValueFromFragmentUsingInterface(String sourFrag) {
        taskDetailFragment =(ProjectDisplayFragment)getFragmentManager().findFragmentById(R.id.content_frame);
        //taskDetailFragment.updateinfo(sourFrag);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            createListDialog();
            return true;
        }

        if (id == R.id.action_refresh) {
            Client client = new Client();
            client.refresh(dataManager.userList.get(sharedPreferences.getString("userKey","missing")));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createListDialog(){

        //create list view for tasks and conferences on each day
        addlist = new ListView(this);
        String[] items ={"Project","Task","Conference"};
        Integer[] icon = new Integer[]{R.drawable.plus};
        ArrayAdapter<String> adapter = new ArrayAdapterWithIcon(this,items,icon);
        addlist.setAdapter(adapter);
        //setup AlertDialog and AlertDialgo Builder
        AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(MainActivity.this);
        DialogBuilder.setCancelable(true);
        DialogBuilder.setPositiveButton("OK",null);
        DialogBuilder.setView(addlist);
        final AlertDialog dialog = DialogBuilder.create();
        dialog.show();

        //Specifiy action about clicking the item in the list
        addlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    final Bundle bundle = new Bundle();
                    switch (position) {
                        //Project
                        case 0:
                            //pass event type to **addEventFragment**
                            bundle.putString("eventtype", "Project");
                            addEventFragment.setArguments(bundle);
                            FragmentManager projectManager = getFragmentManager();
                            FragmentTransaction projectTranscation = projectManager.beginTransaction();
                            projectTranscation.replace(R.id.content_frame, addEventFragment);
                            projectTranscation.addToBackStack(null);
                            projectTranscation.commit();
                            dialog.cancel();
                            break;
                        //Task
                        case 1:
                            //pass event type to **addEventFragment**
                            bundle.putString("eventtype", "Task");
                            addEventFragment.setArguments(bundle);
                            ListView projectlist = new ListView(getApplicationContext());
                            //Find list of the existing project.
                            List<String> projects = new ArrayList<String>();
                            for(HashMap.Entry<String,Project> entry : dataManager.projectList.entrySet()){
                                projects.add(entry.getValue().name);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.add_list_layout, R.id.addList_testview, projects);
                            projectlist.setAdapter(adapter);
                            //Set up AlertDialog again
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            dialogBuilder.setCancelable(true);
                            dialogBuilder.setPositiveButton("OK", null);
                            dialogBuilder.setView(projectlist);
                            final AlertDialog taskdialog = dialogBuilder.create();
                            taskdialog.show();
                            dialog.cancel();

                            projectlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //pass project name to ***AddEventFragment***
                                    String selected = ((TextView) view.findViewById(R.id.addList_testview)).getText().toString();
                                    bundle.putString("projectname", selected);

                                    FragmentManager taskManager = getFragmentManager();
                                    FragmentTransaction taskTranscation = taskManager.beginTransaction();
                                    taskTranscation.replace(R.id.content_frame, addEventFragment);
                                    taskTranscation.addToBackStack(null);
                                    taskTranscation.commit();
                                    taskdialog.cancel();
                                }
                            });

                            break;
                        //Conference
                        case 2:
                            //pass event type to **AddEventFragment**
                            AddConferenceFragment conferenceFragment = new AddConferenceFragment();
                            bundle.putString("eventtype", "Conference");
                            conferenceFragment.setArguments(bundle);
                            FragmentManager conferenceManager = getFragmentManager();
                            FragmentTransaction conferenceTransaction = conferenceManager.beginTransaction();
                            conferenceTransaction.replace(R.id.content_frame, conferenceFragment);
                            conferenceTransaction.addToBackStack(null);
                            conferenceTransaction.commit();
                            dialog.cancel();

                    }
                }catch (NullPointerException e){
                    System.out.print(e.toString());
                }
            }
        });

    }

}
