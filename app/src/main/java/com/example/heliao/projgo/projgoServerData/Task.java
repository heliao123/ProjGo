package com.example.heliao.projgo.projgoServerData;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by HeLiao on 4/2/2016.
 */

public class Task implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 468408715514705317L;
    public String name,description,id,progress,start_time,end_time;
    public HashMap<String,String> participant = new HashMap<String,String>();
    public Project project;
    public Date last_update;
    //	SimpleDateFormat onlyDate = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
    public Task(){
        name = "sample_name";
        description = "sample_discription";
        progress = "";
        update();
    }
    public Task (Project p,String n,String d,String st, String et){
        project = p;
        name = n;
        description = d;
        progress = "";
        start_time = st;
        end_time =et;
        update();
        id = n+last_update.toString();
    }/*
    public void add_participant (User u){
        participant.put(u.userId, u);
        update();
    }*/
    public void delete_participant (User u){
        participant.remove(u.userId);
        update();
    }

    public boolean is_holder (User u){
        if (u.equals(project.holder)){
            return true;
        }
        return false;
    }
    public void update (){
        last_update = new Date();
    }
}
