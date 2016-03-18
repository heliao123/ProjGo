package com.example.heliao.projgo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public final class MainActivity extends AppCompatActivity implements FragmentConnector{

    CalendarFragment calendarfragment;
    TaskListFragment tasklistfragment;
    TaskDetailFragment taskDetailFragment;
    FragmentManager fragmentManager_main = getFragmentManager();
    FragmentTransaction fragmentTransaction_main = fragmentManager_main.beginTransaction();
    AddEventFragment addEventFragment = new AddEventFragment();
    ListView addlist;
    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainFragment = new MainFragment();
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
        taskDetailFragment =(TaskDetailFragment)getFragmentManager().findFragmentById(R.id.content_frame);
        taskDetailFragment.updateinfo(sourFrag);

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
                switch (position){
                    //Project
                    case 0:
                        Toast.makeText(MainActivity.this, "Project 0", Toast.LENGTH_LONG).show();
                        FragmentManager projectManager = getFragmentManager();
                        FragmentTransaction projectTranscation = projectManager.beginTransaction();
                        projectTranscation.replace(R.id.content_frame,addEventFragment);
                        projectTranscation.addToBackStack(null);
                        projectTranscation.commit();
                        dialog.cancel();
                        break;
                    //Task
                    case 1:
                        Toast.makeText(MainActivity.this, "Task 1", Toast.LENGTH_LONG).show();
                        ListView projectlist = new ListView(getApplicationContext());
                        String [] projects={"Project1","Project2","Project3","Prjoect4"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.add_list_layout,R.id.addList_testview,projects);
                        projectlist.setAdapter(adapter);
                        //Set up AlertDialog again
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        dialogBuilder.setCancelable(true);
                        dialogBuilder.setPositiveButton("OK",null);
                        dialogBuilder.setView(projectlist);
                        final AlertDialog taskdialog = dialogBuilder.create();
                        taskdialog.show();
                        dialog.cancel();

                        projectlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                        Toast.makeText(MainActivity.this, "Conference 2", Toast.LENGTH_LONG).show();
                        FragmentManager conferenceManager = getFragmentManager();
                        FragmentTransaction conferenceTransaction = conferenceManager.beginTransaction();
                        conferenceTransaction.replace(R.id.content_frame,addEventFragment);
                        conferenceTransaction.addToBackStack(null);
                        conferenceTransaction.commit();
                        dialog.cancel();

                }
            }
        });

    }

}
