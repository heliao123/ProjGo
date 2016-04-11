package com.example.heliao.projgo.projgoServerData;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by HeLiao on 4/2/2016.
 */
public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7948595295595593068L;
    public String userId, password;
    public static HashMap<String,Project> project = new HashMap<String,Project>();
    public static HashMap<String,Task> task = new HashMap<String, Task>();
    public static HashMap<String,Conference> conference = new HashMap<String,Conference>();
    public Date last_update;
    public User(){
        userId = "ZhangSan";
        password = "123123";
        update();
    }
    public User(String x, String y){
        userId = x;
        password = y;
        update();
    }
    public static boolean match(String id, String pw, HashMap<String,User> user_list){
        if (user_list.containsKey(id) && user_list.get(id).password.equals(pw)){
            return true;
        }
        return false;
    }
    public void add_project(Project p){
        project.put(p.id, p);
        update();
    }
    public void add_task (Task t){
        task.put(t.id, t);
        update();
    }
    public void add_conference (Conference c){
        conference.put(c.id, c);
        update();
    }
    public void delete_project(Project p){
        project.remove(p.id);
        update();
    }
    public void delete_task (Task t){
        task.remove(t.id);
        update();
    }
    public void delete_conference (Conference c){
        conference.remove(c.id);
        update();
    }
    public void update (){
        last_update = new Date();
    }
}
