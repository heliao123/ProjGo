package com.example.heliao.projgo.projgoServerData;

import java.io.Serializable;
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
    public Date last_update,start_time,end_time;
    public User holder;
    //	SimpleDateFormat onlyDate = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
    public Project(){
        name = "sample_name";
        description = "sample_discription";
        update();
    }
    public Project(User u,String n,String d, String sd, String ed){
        holder = u;
        name = n;
        description = d;
        startdate = sd;
        enddate =ed;
        update();
        id = n+last_update.toString();
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
        if (u.equals(holder)){
            return true;
        }
        return false;
    }
    public void update (){
        last_update = new Date();
    }
}