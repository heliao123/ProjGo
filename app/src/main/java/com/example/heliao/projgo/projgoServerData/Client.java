package com.example.heliao.projgo.projgoServerData;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
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


public class Client {
	static ObjectOutputStream oos = null;
	static ObjectInputStream ois = null;
	static User curr_user = new User();
	static Project curr_proj = new Project();
	static Task curr_task = new Task();
	static Conference curr_conf = new Conference();
	static Packet pack = new Packet();
	static Packet pack_re = new Packet();
	static HashMap<String,Project> project = new HashMap<String,Project>();
	static HashMap<String,Task> task = new HashMap<String,Task>();
	static HashMap<String,Conference> conference = new HashMap<String,Conference>();
	public static void main(String[] args) throws IOException {
		String frame = "log_in_menu";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat ftt = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
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
					pack = new Packet(userId,password);
					pack.request = Packet.log_in;
					sendpack();
					if (pack_re.request == Packet.log_in){
						frame = "main_menu";
					}
					else {
						frame = "id_password_not_match";
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
					sendpack();
					if (pack_re.request==Packet.reg){
						frame = "reg_success";
					}
					else{
						frame = "reg_fail";
					}
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
							refresh();
							Iterator<Map.Entry<String,Project>> it = curr_user.project.entrySet().iterator();
							while (it.hasNext()){
								Map.Entry<String,Project> temp =it.next();
								System.out.println(temp.getValue().id);
							}
							if (curr_user.project!=null&&!curr_user.project.isEmpty()){
								System.out.println("Which one do you want to open?");
								String choose_proj = br.readLine();
								if (curr_user.project.containsKey(choose_proj)){
									curr_proj = curr_user.project.get(choose_proj);
									System.out.println("Name: "+curr_proj.name);
									System.out.println("Description: "+curr_proj.description);
									System.out.println("Duration: from "+curr_proj.start_time.toString()+" to "+curr_proj.end_time.toString());
									System.out.println("Holder: "+curr_proj.holder.userId);
									System.out.println("Task list:");
									Iterator<Map.Entry<String,Task>> ite = curr_proj.task.entrySet().iterator();
									while (ite.hasNext()){
										Map.Entry<String,Task> temp = ite.next();
										System.out.println("  "+temp.getValue().id);
									}
									System.out.println("Do you want to choose any task? Yes/No");
									if (br.readLine().equals("Yes")){
										System.out.println("Which task do you want to choose?");
										String choose_task = br.readLine();
										if (curr_proj.task.containsKey(choose_task)){
											curr_task = curr_proj.task.get(choose_task);
											System.out.println("Name: "+curr_task.name);
											System.out.println("Description: "+curr_task.description);
											System.out.println("Duration: from "+curr_task.start_time_string.toString()+" to "+curr_task.end_time_string.toString());
											System.out.println("Holder: "+curr_task.project.holder.userId);
											System.out.println("Project: "+curr_task.project.name);
											System.out.println("Progress: "+curr_task.progress);
											if (curr_task.is_holder(curr_user)){
												System.out.println("Do you want to modify this task? Yes/No");
												if (br.readLine().equals("Yes")){
													frame = "modify_task";
													break;
												}
											}
											else {
												System.out.println("Do you want to update progress? Yes/No");
												if (br.readLine().equals("Yes")){
													frame = "update_progress";
													break;
												}
											}
										}
										else {
											System.out.println("This task does not exist");
										}
									}
								}
								else {
									System.out.println("This Project does not exist");
								}
								if (curr_user.project.get(choose_proj).is_holder(curr_user)){
									System.out.println("Do you want to modify this project? Yes/No");
									if (br.readLine().equals("Yes")){
										frame = "modify_project";
										break;
									}
									System.out.println("Do you want to delete this project? Yes/No");
									if (br.readLine().equals("Yes")){
										frame = "delete_project";
										break;
									}
								}
								else {
									System.out.println("Do you want to quit? Yes/No");
									if (br.readLine().equals("Yes")){
										frame = "modify_project";
										break;
									}
								}
							}
							break;

						case "choose task":
							System.out.println("Your choice: "+m);
							refresh();
							Iterator<Map.Entry<String,Task>> itt = curr_user.task.entrySet().iterator();
							while (itt.hasNext()){
								Map.Entry<String,Task> temp = itt.next();
								System.out.println(temp.getValue().id);
							}
							if (curr_user.task != null&&!curr_user.task.isEmpty()){
								System.out.println("Which one do you want to open?");
								String choose_task = br.readLine();
								if (curr_user.task.containsKey(choose_task)){
									curr_task = curr_user.task.get(choose_task);
									System.out.println("Name: "+curr_task.name);
									System.out.println("Description: "+curr_task.description);
									System.out.println("Holder: "+curr_task.project.holder.userId);
									System.out.println("Project: "+curr_task.project.name);
									System.out.println("Progress: "+curr_task.progress);
									if (curr_task.is_holder(curr_user)){
										System.out.println("Do you want to modify this task? Yes/No");
										if (br.readLine().equals("Yes")){
											frame = "modify_task";
											break;
										}
										System.out.println("Do you want to delete this task? Yes/No");
										if (br.readLine().equals("Yes")){
											frame = "delete_task";
											break;
										}
									}
									else {
										System.out.println("Do you want to update progress? Yes/No");
										if (br.readLine().equals("Yes")){
											frame = "update_progress";
											break;
										}
										System.out.println("Do you want to quit? Yes/No");
										if (br.readLine().equals("Yes")){
											frame = "modify_task";
											break;
										}
									}
								}else {
									System.out.println("This task does not exist");
								}
							}
							break;

						case "choose conference":
							System.out.println("Your choice: "+m);
							refresh();
							Iterator<Map.Entry<String,Conference>> ittt = curr_user.conference.entrySet().iterator();
							while (ittt.hasNext()){
								Map.Entry<String,Conference> temp = ittt.next();
								System.out.println(temp.getValue().id);
							}
							if (curr_user.conference!=null&&!curr_user.conference.isEmpty()){
								System.out.println("Which one do you want to open?");
								String choose_conf = br.readLine();
								if (curr_user.conference.containsKey(choose_conf)){
									curr_conf = curr_user.conference.get(choose_conf);
									System.out.println("Name: "+curr_conf.name);
									System.out.println("Description: "+curr_conf.description);
									System.out.println("Location: "+curr_conf.location);
									System.out.println("Holder: "+curr_conf.holder.userId);
									if (curr_conf.is_holder(curr_user)){
										System.out.println("Do you want to modify this conference? Yes/No");
										if (br.readLine().equals("Yes")){
											frame = "modify_conference";
											break;
										}
										System.out.println("Do you want to delete this conference? Yes/No");
										if (br.readLine().equals("Yes")){
											frame = "delete_conference";
											break;
										}
									}
									else {
										System.out.println("Do you want to quit? Yes/No");
										if (br.readLine().equals("Yes")){
											frame = "modify_conference";
											break;
										}
									}
								}
							}
							break;

						case "log out":
							System.out.println("Your choice: "+m);
							frame = "log_in_menu";
							break;

						default:                                //every 30 seconds if no reaction, then refresh.
						case "refresh":
							System.out.println("Your choice: "+m);
							frame = "main_menu";
							refresh();
							break;
					}
					break;

				case "new_project":
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.new_proj;
					pack.project = new Project();
					System.out.println("What is the name of this project?");
					pack.project.name = br.readLine();
					pack.project.id = pack.project.name+"_"+pack.project.new_time.getTime();
					System.out.println("What is the description of this project?");
					pack.project.description = br.readLine();
					System.out.println("When will this project start? yyyy-MM-dd");
					try {
						pack.project.start_time = ft.parse(br.readLine());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					System.out.println("When will this project end? yyyy-MM-dd");
					try {
						pack.project.end_time = ft.parse(br.readLine());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					System.out.println("How many people will participate?");
					String nums = br.readLine();
					int num = Integer.valueOf(nums);
					System.out.println("Who will participate in this project?");
					pack.participant_list = new ArrayList<String>();
					for(int a=0;a<num;a++){
						String temp = br.readLine();
						pack.participant_list.add(temp);
					}
					sendpack();
					break;

				case "new_task":
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.new_task;
					pack.task = new Task();
					System.out.println("What is the name of this task?");
					pack.task.name = br.readLine();
					pack.task.id = pack.task.name + "_"+pack.task.new_time.getTime();
					System.out.println("What is the description of this task?");
					pack.task.description = br.readLine();
					System.out.println("When will this task start? yyyy-MM-dd");
					/*try {
						pack.task.start_time_string = ft.parse(br.readLine());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					System.out.println("When will this task end? yyyy-MM-dd");
					try {
						pack.task.end_time_string = ft.parse(br.readLine());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}*/
					System.out.println("Which project does this task belong to?");
					pack.task.project = new Project();
					pack.task.project.id = br.readLine();
					System.out.println("How many people will participate?");
					nums = br.readLine();
					num = Integer.valueOf(nums);
					pack.participant_list = new ArrayList<String>();
					System.out.println("Who will participate in this task?");
					for(int a=0;a<num;a++){
						pack.participant_list.add(br.readLine());
					}
					sendpack();
					break;

				case "new_conference":
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.new_conf;
					pack.conference = new Conference();
					System.out.println("What is the name of this conf?");
					pack.conference.name = br.readLine();
					pack.conference.id = pack.conference.name+"_"+pack.conference.new_time.getTime();
					System.out.println("What is the description of this conf?");
					pack.conference.description = br.readLine();
					System.out.println("Where will this conf take place?");
					pack.conference.location = br.readLine();
					System.out.println("When will this conf start? yyyy-MM-dd HH:mm");
					/*try {
						pack.conference.start_time_string = ftt.parse(br.readLine());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					System.out.println("When will this conf end? yyyy-MM-dd HH:mm");
					try {
						pack.conference.end_time_string = ftt.parse(br.readLine());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}*/
					System.out.println("How many people will participate?");
					nums = br.readLine();
					num = Integer.valueOf(nums);
					pack.participant_list = new ArrayList<String>();
					System.out.println("Who will participate in this conf?");
					for(int a=0;a<num;a++){
						pack.participant_list.add(br.readLine());
					}
					sendpack();
					break;

				case "modify_project":              //modify & quit
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.mod_proj;
					pack.project = curr_proj;
					if (curr_proj.is_holder(curr_user)) {
						System.out.println("What is the new name of this project?");
						pack.project.name = br.readLine();
						System.out.println("What is the new description of this project?");
						pack.project.description = br.readLine();
						System.out.println("When will this project start? yyyy-MM-dd");
						try {
							pack.project.start_time = ft.parse(br.readLine());
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						System.out.println("When will this project end? yyyy-MM-dd");
						try {
							pack.project.end_time = ft.parse(br.readLine());
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
					else {
						System.out.println("You want to quit this project.");
						pack.project.participant.remove(curr_user.userId);
					}
					sendpack();
					break;

				case "modify_task":              //modify & quit
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.mod_task;
					pack.task = curr_task;
					if (curr_task.is_holder(curr_user)){
						System.out.println("What is the new name of this task?");
						pack.task.name = br.readLine();
						System.out.println("What is the new description of this task?");
						pack.task.description = br.readLine();
						System.out.println("When will this task start? yyyy-MM-dd");
						/*try {
							pack.task.start_time_string = ft.parse(br.readLine());
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						System.out.println("When will this task end? yyyy-MM-dd");
						try {
							pack.task.end_time_string = ft.parse(br.readLine());
						} catch (ParseException e1) {
							e1.printStackTrace();
						}*/
					}
					else {
						System.out.println("You want to quit this task.");
						pack.task.participant.remove(curr_user.userId);
					}
					sendpack();
					break;

				case "update_progress":
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.up_prog;
					pack.task = curr_task;
					System.out.println("What is the new progress?");
					pack.task.progress = br.readLine();
					sendpack();
					break;

				case "modify_conference":              //modify & quit
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.mod_conf;
					pack.conference = curr_conf;
					if (curr_conf.is_holder(curr_user)){
						System.out.println("What is the new name of this conf?");
						pack.conference.name = br.readLine();
						System.out.println("What is the new description of this conf?");
						pack.conference.description = br.readLine();
						System.out.println("When will this project start? yyyy-MM-dd");
						/*try {
							pack.conference.start_time_string = ftt.parse(br.readLine());
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						System.out.println("When will this conf end? yyyy-MM-dd HH:mm");
						try {
							pack.conference.end_time_string = ftt.parse(br.readLine());
						} catch (ParseException e1) {
							e1.printStackTrace();
						}*/
					}
					else {
						System.out.println("You want to quit this conf.");
						pack.conference.participant.remove(curr_user.userId);
					}
					sendpack();
					break;

				case "delete_project":
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.del_proj;
					pack.project = curr_proj;
					sendpack();
					break;

				case "delete_task":
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.del_task;
					pack.task = curr_task;
					sendpack();
					break;

				case "delete_conference":
					System.out.println(frame);
					frame = "main_menu";
					pack = new Packet(curr_user.userId,curr_user.password);
					pack.request = Packet.del_conf;
					pack.conference = curr_conf;
					sendpack();
					break;
			}
		}
	}

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
			System.out.println(pack_re.error_type);;
			if (pack_re.request!=Packet.error){
				curr_user = pack_re.user;
				project = (HashMap<String, Project>) pack_re.user.project.clone();
				task = (HashMap<String, Task>) pack_re.user.task.clone();
				conference = (HashMap<String, Conference>) pack_re.user.conference.clone();
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

	public static void refresh(){
		pack = new Packet(curr_user.userId,curr_user.password);
		pack.request = Packet.refresh;
		curr_user = new User();
		project = new HashMap<String,Project>();
		task = new HashMap<String,Task>();
		conference = new HashMap<String,Conference>();
		sendpack();
	}
}
