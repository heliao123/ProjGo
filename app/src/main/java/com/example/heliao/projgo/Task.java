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

public class Task implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 468408715514705317L;
    public String name,description,id,progress, start_time_string, end_time_string;
    public HashMap<String,String> participant = new HashMap<String,String>();
    public ArrayList<String> participant_list = new ArrayList<String>();
    public Project project;
    public Date last_update,new_time,start_time,end_time;
    //	SimpleDateFormat onlyDate = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
    public Task(){
        name = "sample_name";
        description = "sample_discription";
        progress = "";
        new_time = new Date();
        last_update = new_time;
    }
    public Task (Project p,String n,String d,String st, String et){
        project = p;
        name = n;
        description = d;
        progress = "";
        start_time_string = st;
        end_time_string =et;
        new_time = new Date();
        last_update = new_time;
        id = n+"_"+last_update.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            start_time = sdf.parse(st);
            end_time = sdf.parse(et);
        }catch (ParseException e){
            e.printStackTrace();
        }
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
        if (u.userId.equals(project.holder.userId)){
            return true;
        }
        return false;
    }
    public void update (){
        last_update = new Date();
    }
}
