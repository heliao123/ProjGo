package com.example.heliao.projgo.projgoServerData;

import java.io.Serializable;
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
    public String name,description,location,id, start_time, end_time, conferencedate;
    public HashMap<String,String> participant = new HashMap<String,String>();
    public User holder;
    public Date last_update;
    //	SimpleDateFormat onlyDate = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
    public Conference (){
        name = "sample_name";
        location = "sample_location";
        description = "sample_discription";
        update();
    }
    public Conference (User u,String n,String d, String st, String et,String date){
        holder = u;
        name = n;
        //location = l;
        description = d;
        start_time = st;
        end_time =et;
        conferencedate = date;
        update();
        id = n + last_update.toString();
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
        if (u.equals(holder)){
            return true;
        }
        return false;
    }
    public void update (){
        last_update = new Date();
    }
}
