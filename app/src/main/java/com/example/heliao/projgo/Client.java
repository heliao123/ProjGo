package com.example.heliao.projgo;
import com.example.heliao.projgo.Conference;
import com.example.heliao.projgo.Project;
import com.example.heliao.projgo.Task;
import com.example.heliao.projgo.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class Client {
    static ObjectOutputStream oos = null;
    static ObjectInputStream ois = null;
    static Packet pack = new Packet();
    static Packet pack_re = new Packet();

    @SuppressWarnings("unchecked")
    public static void sendpack(){
        try {
            URL realUrl = new URL("http://csci6221.appspot.com/");
//			URL realUrl = new URL("http://localhost:8080/");
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; sv1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            oos = new ObjectOutputStream(conn.getOutputStream());
            oos.writeObject(pack);
            oos.flush();
            ois = new ObjectInputStream(conn.getInputStream());
            pack_re = (Packet) ois.readObject();
            //System.out.println(pack_re.error_type);
            if (pack_re.request!=Packet.error){
                ServerDataManager.userList.clear();
                ServerDataManager.userList.put(pack_re.user.userId, pack_re.user);
                ServerDataManager.projectList.clear();
                ServerDataManager.projectList = (HashMap<String, Project>) pack_re.user.project.clone();
                ServerDataManager.taskList.clear();
                ServerDataManager.taskList = (HashMap<String, Task>) pack_re.user.task.clone();
                ServerDataManager.conferenceList.clear();
                ServerDataManager.conferenceList = (HashMap<String, Conference>) pack_re.user.conference.clone();
            }
            else {
                System.out.println("Error: "+pack_re.error_type);;
            }
        } catch (Exception e) {
            System.out.println("Post Error!"+e);
            sendpack();
            e.printStackTrace();
        }
        finally{
            try{
                if(oos!=null){
                    oos.close();
                }
                if (ois!=null){
                    ois.close();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public static void reg(User u){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.reg;
        sendpack();
    }

    public static void log_in(User u){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.log_in;
        sendpack();
    }

    public static void new_proj(User u,Project pr){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.new_proj;
        pack.project = pr;
        pack.participant_list = pr.participant_list;
        sendpack();
    }

    public static void new_task(User u,Task t){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.new_task;
        pack.task = t;
        pack.participant_list = t.participant_list;
        sendpack();
    }

    public static void new_conf(User u,Conference c){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.new_conf;
        pack.conference = c;
        pack.participant_list = c.participant_list;
        sendpack();
    }

    public static void mod_proj(User u,Project pr){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.mod_proj;
        pack.project = pr;
        sendpack();
    }

    public static void mod_task(User u,Task t){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.mod_task;
        pack.task = t;
        sendpack();
    }

    public static void up_prog(User u,Task t){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.up_prog;
        pack.task = t;
        sendpack();
    }

    public static void mod_conf(User u,Conference c){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.mod_conf;
        pack.conference = c;
        sendpack();
    }

    public static void del_proj(User u,Project pr){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.del_proj;
        pack.project = pr;
        sendpack();
    }

    public static void del_task(User u,Task t){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.del_task;
        pack.task = t;
        sendpack();
    }

    public static void del_conf(User u,Conference c){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.del_conf;
        pack.conference = c;
        sendpack();
    }

    public static void refresh(User u){
        pack = new Packet(u.userId,u.password);
        pack.request = Packet.refresh;
        ServerDataManager.userList.clear();
        ServerDataManager.projectList.clear();
        ServerDataManager.taskList.clear();
        ServerDataManager.conferenceList.clear();
        sendpack();
    }
}



class Packet implements Serializable {
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
        public Packet(){
            num_of_proj = 0;
            num_of_task = 0;
            num_of_conf = 0;
            request_time = new Date();
        }
        public Packet(int p,int t,int c){
            num_of_proj = p;
            num_of_task = t;
            num_of_conf = c;
            request_time = new Date();
        }
        public Packet(String u,String pw){
            userId = u;
            password = pw;
            num_of_proj = 0;
            num_of_task = 0;
            num_of_conf = 0;
            request_time = new Date();
        }
    }

