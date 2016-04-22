package com.example.heliao.projgo;

import com.example.heliao.projgo.Conference;
import com.example.heliao.projgo.Project;
import com.example.heliao.projgo.Task;
import com.example.heliao.projgo.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HeLiao on 4/22/2016.
 */

public class Packet implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1960475877741349408L;
    final static int reg = 0;
    final static int log_in = 1;
    final static int new_proj = 2;
    final static int new_task = 3;
    final static int new_conf = 4;
    final static int mod_proj = 5;
    final static int mod_task = 6;
    final static int up_prog = 7;
    final static int mod_conf = 8;
    final static int del_proj = 9;
    final static int del_task = 10;
    final static int del_conf = 11;
    final static int refresh = 12;
    final static int error = 13;
    public String userId, password, error_type;
    public int request,num_of_proj, num_of_task, num_of_conf;
    public ArrayList<String> participant_list,rmv_proj,rmv_task,rmv_conf;
    public User user;
    public Project project;
    public Task task;
    public Conference conference;
    public Date request_time;
    Packet(){
        num_of_proj = 0;
        num_of_task = 0;
        num_of_conf = 0;
        request_time = new Date();
    }
    Packet(int p,int t,int c){
        num_of_proj = p;
        num_of_task = t;
        num_of_conf = c;
        request_time = new Date();
    }
    Packet(String u,String pw){
        userId = u;
        password = pw;
        num_of_proj = 0;
        num_of_task = 0;
        num_of_conf = 0;
        request_time = new Date();
    }
}

