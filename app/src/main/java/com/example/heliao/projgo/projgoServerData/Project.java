package com.example.heliao.projgo.projgoServerData;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by HeLiao on 4/2/2016.
 */
public class Project implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5997969113587524502L;
    public String name,description,id, startdate, enddate;
    public HashMap<String,String> participant = new HashMap<String,String>();
    public HashMap<String,Task> task = new HashMap<String,Task>();
    public ArrayList<String> participant_list = new ArrayList<String>();
    public Date last_update,start_time,end_time,new_time;
    public User holder;
    //	SimpleDateFormat onlyDate = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
    public Project(){
        name = "sample_name";
        description = "sample_discription";
        new_time = new Date();
        last_update = new_time;
    }
    public Project(User u,String n,String d, String sd, String ed)  {
        holder = u;
        name = n;
        description = d;
        startdate = sd;
        enddate =ed;
        new_time = new Date();
        last_update = new_time;
        id = n+"_"+last_update.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            start_time = sdf.parse(sd);
            end_time = sdf.parse(ed);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }/*
    public void add_participant (User u){
        participant.put(u.userId, u);
        update();
    }*/
    public void add_task (Task t){
        task.put(t.id, t);
        update();
    }
    public void delete_participant (User u){
        participant.remove(u.userId);
        update();
    }
    public void delete_task (Task t){
        task.remove(t.id);
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