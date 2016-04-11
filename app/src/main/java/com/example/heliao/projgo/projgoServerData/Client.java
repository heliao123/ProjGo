package com.example.heliao.projgo.projgoServerData;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;






class Packet implements Serializable{
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
	final static int mod_conf = 7;
	final static int error = 8;
	public String userId, password, error_type;
	public int request,num_of_proj, num_of_task, num_of_conf;
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

public class Client {

	public static void main(String[] args) throws IOException {
		String frame = "log_in_menu";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		User curr_user = new User();
		while (true){
			switch (frame)
			{
				case "log_in_menu":
					System.out.println(frame);
					System.out.println("What do you want to do?\n log in\n register");
					String x = br.readLine();
					if (x.equals("log in")){
						frame = "log_in";
						break;
					}
					else if (x.equals("register")){
						frame = "register";
						break;
					}
					else {
						System.out.println("You enter Wrong");
						break;
					}
					
				case "log_in":
					System.out.println("Please enter your userID:");
					String userId = br.readLine();
					System.out.println("Please enter your password:");
					String password = br.readLine();
					Packet pack = new Packet(userId,password);
					pack.request = Packet.log_in;
					Packet pack_re;
					try {
						URL realUrl = new URL("http://csci6221.appspot.com/");
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
						if (pack_re.request==Packet.log_in){
							frame = "main_menu";
							int proj = pack_re.num_of_proj;
							int task = pack_re.num_of_task;
							int conf = pack_re.num_of_conf;
							curr_user = new User(userId,password);
							for (int i=0;i<proj;i++){
								Project temp = (Project) ois.readObject();
								curr_user.project.put(temp.id, temp);
							}
							for (int i=0;i<task;i++){
								Task temp = (Task) ois.readObject();
								curr_user.task.put(temp.id, temp);
							}
							for (int i=0;i<conf;i++){
								Conference temp = (Conference) ois.readObject();
								curr_user.conference.put(temp.id, temp);
							}
						}
						else {
							frame = "id_password_not_match";
						}
					} catch (Exception e) {
						System.out.println("Post Error!"+e);
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
					break;
					
				case "id_password_not_match":
					System.out.println(frame);
					System.out.println("Wrong userID or password\nPlease log in again");
					frame = "log_in";
					break;
					
				case "register":
					System.out.println(frame);
					System.out.println("Please enter your userID:");
					userId = br.readLine();
					System.out.println("Please enter your password:");
					password = br.readLine();
					pack = new Packet(userId,password);
					pack.request = Packet.reg;
					try {
						URL realUrl = new URL("http://csci6221.appspot.com/");
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
						if (pack_re.request==Packet.reg){
							frame = "reg_success";
						}
						else {
							frame = "reg_fail";
						}
					} catch (Exception e) {
						System.out.println("Post Error!"+e);
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
					
					frame = "reg_success";
					break;
					
				case "reg_success":
					System.out.println(frame);
					System.out.println("Thank you for register");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Post Error!"+e);
						e.printStackTrace();
					}
					frame = "log_in";
					break;
					
				case "reg_fail":
					System.out.println(frame);
					System.out.println("Your userId is not avaliable\nPlease try again");
					frame = "register";
					break;
					
				case "main_menu":
					System.out.println(frame);
					System.out.println("What do you want to do?\n new project\n new task\n new conference\n choose project\n choose task\n chooose conference\n log out\n refresh");
					String m = br.readLine();
					switch (m)
					{
						case "new project":
							System.out.println("Your choice: "+m);
							frame = "new_project";
							break;
						case "new task":
							System.out.println("Your choice: "+m);
							frame = "new_task";
							break;
						case "new conference":
							System.out.println("Your choice: "+m);
							frame = "new_conference";
							break;
						case "choose project":
							System.out.println("Your choice: "+m);
							Iterator<String> it = curr_user.project.keySet().iterator();
							while (it.hasNext()){
								System.out.println(curr_user.project.get(it.next()).name);
							}
							break;
						case "choose task":
							System.out.println("Your choice: "+m);
							
							break;
						case "choose conference":
							System.out.println("Your choice: "+m);
							
							break;
						case "log out":
							System.out.println("Your choice: "+m);
							frame = "log_in_menu";
							break;
						case "refresh":
							System.out.println("Your choice: "+m);
							frame = "main_menu";
							pack = new Packet(curr_user.userId,curr_user.password);
							pack.request = Packet.log_in;
							try {
								URL realUrl = new URL("http://csci6221.appspot.com/");
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
								if (pack_re.request==Packet.log_in){
									frame = "main_menu";
									int proj = pack_re.num_of_proj;
									int task = pack_re.num_of_task;
									int conf = pack_re.num_of_conf;
									for (int i=0;i<proj;i++){
										Project temp = (Project) ois.readObject();
										curr_user.project.put(temp.id, temp);
									}
									for (int i=0;i<task;i++){
										Task temp = (Task) ois.readObject();
										curr_user.task.put(temp.id, temp);
									}
									for (int i=0;i<conf;i++){
										Conference temp = (Conference) ois.readObject();
										curr_user.conference.put(temp.id, temp);
									}
								}
								else {
									System.out.println("Error: "+pack_re.error_type);;
								}
							} catch (Exception e) {
								System.out.println("Post Error!"+e);
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
							break;
					}
					break;
					
				case "new_project":
					System.out.println(frame);
					
					break;
					
				case "new_task":
					System.out.println(frame);
					
					break;
					
				case "new_conference":
					System.out.println(frame);
					
					break;
					
				case "modify_project":
					System.out.println(frame);
					
					break;
					
				case "modify_task":
					System.out.println(frame);
					
					break;
					
				case "update_progress":
					System.out.println(frame);
					
					break;
					
				case "modify_conference":
					System.out.println(frame);
					
					break;
					
			}
		}
	}
}
