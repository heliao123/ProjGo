package com.example.heliao.projgo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by HeLiao on 4/2/2016.
 */

public class Conference implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6411148402757655234L;
    public String name,description,location,id, start_time_string, end_time_string, conferencedate;
    public HashMap<String,String> participant = new HashMap<String,String>();
    public ArrayList<String> participant_list = new ArrayList<String>();
    public User holder;
    public Date last_update,new_time,start_time,end_time,conDate;
    //	SimpleDateFormat onlyDate = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
    public Conference (){
        name = "sample_name";
        location = "sample_location";
        description = "sample_discription";
        new_time = new Date();
        last_update = new_time;
    }
    public Conference (User u,String n,String d, String st, String et,String date){
        holder = u;
        name = n;
        //location = l;
        description = d;
        start_time_string = date +" "+ st;
        end_time_string =date + " " +et;
        conferencedate = date;
        new_time = new Date();
        last_update = new_time;
        id = n + last_update.toString();
        SimpleDateFormat sdfdate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        SimpleDateFormat sdftime = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        try {
            start_time = sdftime.parse(st);
            end_time = sdftime.parse(et);
            conDate = sdfdate.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        
    }
    /*
    public void add_participant (User u){
        participant.put(u.userId, u);
        update();
    }*/
    public void delete_participant (User u){
        participant.remove(u.userId);
        update();
    }
    public boolean is_holder (User u){
        if (u.userId.equals(holder.userId)){
            return true;
        }
        return false;
    }
    public void update (){
        last_update = new Date();
    }
}
