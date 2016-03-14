package com.example.heliao.projgo;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class MainActivity extends AppCompatActivity {

    private CalendarView calendar;
    ListView todayTask_ListView;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //create calendar on the main page
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                Toast.makeText(getApplicationContext(), month + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                createListDialog(year, month, dayOfMonth);

            }
        });



    }

    public void createListDialog(int year, int month, int date){

        //create list view for tasks and conferences on each day

        todayTask_ListView = new ListView(this);

        String[] items ={"Project","homework1","homework2","homework3","Conference","meeting1","meeting2"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.today_task,R.id.textView,items);

        todayTask_ListView.setAdapter(adapter);
        todayTask_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup vg = (ViewGroup) view;
                TextView txt = (TextView) vg.findViewById(R.id.textView);
                Toast.makeText(MainActivity.this, txt.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        //setup AlertDialog and AlertDialgo Builder
        AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(MainActivity.this);
        DialogBuilder.setTitle(month+"/"+date+"/"+year);
        DialogBuilder.setCancelable(true);
        DialogBuilder.setPositiveButton("OK",null);
        DialogBuilder.setView(todayTask_ListView);
        AlertDialog dialog = DialogBuilder.create();
        dialog.show();

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
            return true;
        }

        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
