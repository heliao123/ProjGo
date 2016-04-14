package com.example.heliao.projgo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by HeLiao on 4/14/2016.
 */
public class DateTimePickDialog {
    Calendar cal;
    int year_x,month_x,day_x, min_x, hour_x;
    Context context;
    EditText editText;

    public DateTimePickDialog( Context context, EditText editText){
        this.context = context;
        this.editText = editText;
    }
    public void showDatePickDialogOnClick() {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                year_x = cal.get(Calendar.YEAR);
                month_x = cal.get(Calendar.MONTH);
                day_x = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editText.setText(monthOfYear+1+"/"+dayOfMonth+"/"+year);
                    }
                },year_x, month_x, day_x);
                dpd.show();
            }
        });
    }

    public void showTimePickDialogOnClick() {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                min_x= cal.get(Calendar.MINUTE);
                hour_x = cal.get(Calendar.HOUR);

              TimePickerDialog tpd = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                  @Override
                  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                      editText.setText(hourOfDay+":"+minute);
                  }
              },hour_x,min_x,true);
                tpd.show();}
        });
    }
}
