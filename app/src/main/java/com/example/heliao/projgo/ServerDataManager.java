package com.example.heliao.projgo;

import java.util.HashMap;

/**
 * Created by HeLiao on 4/2/2016.
 */
public class ServerDataManager {
    static public HashMap<String,User> userList = new HashMap<String,User>();;
    static public HashMap<String,Project> projectList= new HashMap<String, Project>();
    static public HashMap<String,Task> taskList = new HashMap<String, Task>();;
    static public HashMap<String,Conference> conferenceList = new HashMap<String, Conference>();;
    private static ServerDataManager sInstance;

    public void addUser (String username, User newuser){
        userList.put(username,newuser);
    }

    public void addProject(String projectid, Project newproject){
        projectList.put(projectid,newproject);
    }

    public void addTask(String taskid, Task newtask){
        taskList.put(taskid,newtask);
    }

    public void addConference(String conferenceid, Conference newconference){
        conferenceList.put(conferenceid,newconference);
    }

    public static ServerDataManager getInstance(){
        if(sInstance ==null){
            sInstance = new ServerDataManager();
        }
            return sInstance;

    }

}
