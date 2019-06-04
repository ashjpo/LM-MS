package Main.AS.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.dgc.Lease;
import java.util.Random;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;

public class service_rpc extends service{
	Main_Context mc;
	public service_model service_model_obj;
	public boolean if_service_start=false;
	
	Thread service_thread;
	
	//初始化文件路径
	String init_cmd="";
	public service_rpc(service_model service_model_obj,Main_Context mc) {
		super(service_model_obj, mc);
		this.mc=mc;
		this.service_model_obj=service_model_obj;
		this.prepare_service();
		
		
		
	}
	
	private void prepare_service() {
		System.out.println("service prepare >>>"+service_model_obj.language);
		if(service_model_obj.language.equals("php")) {
			init_cmd="php "+this.mc.config_obj.service_php_init_file_path;
		}else if(service_model_obj.language.equals("python")) {
			init_cmd="python "+this.mc.config_obj.service_python_init_file_path;
		}else if(service_model_obj.language.equals("java")) {
			init_cmd="java -jar "+this.mc.config_obj.service_java_init_file_path;
		}
		if(!init_cmd.equals("")) {
			service_thread = new Thread(new Runnable(){

				@Override
				public void run() {
					Runtime runtime = Runtime.getRuntime();
					BufferedReader br;
					try {
						System.out.println(init_cmd+"  "+service_id+"  "+service_port+"  "+service_model_obj.config_json_path);
						//if(service_model_obj.language.equals("php")) {
						br = new BufferedReader(new InputStreamReader(runtime.exec(init_cmd+"  "+service_id+"  "+service_port+"  "+service_model_obj.config_json_path).getInputStream()));
						//br = new BufferedReader(new InputStreamReader(runtime.exec(init_cmd+"  "+service_id+"  "+"7609"+"  "+service_model_obj.config_json_path).getInputStream()));
						//StringBuffer b = new StringBuffer();
			            String line=null;
			            StringBuffer b=new StringBuffer();
			            while ((line=br.readLine())!=null) {
			                b.append(line+"\n");
			            }
			            
			            System.out.println("ERROR>>>"+b.toString());
						service_has_stop(b.toString());
						/*}else  if(service_model_obj.language.equals("java")) {
							init_java_service init_java_service=new init_java_service(service_id, service_port+"", service_model_obj.config_json_path);
							boolean if_start_ok=init_java_service.start_service();
							System.out.println("JAVA SERVICE >>>"+if_start_ok);
							if(!if_start_ok) {
								service_has_stop("error");
							}
						}*/
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
				}
				
			});
			System.out.println("service prepare finish");
		}
		
		
	}
	
	public void service_has_stop(String message) {
		System.out.println("service_has_stop");
		//更改port
		System.out.println("change service port");
		if(mc.service_table_obj.port_list.size()==0) {
			this.service_port=mc.service_table_obj.begin_service_port;
			mc.service_table_obj.port_list.add(this.service_port);
		}else {
			this.service_port=mc.service_table_obj.port_list.get(mc.service_table_obj.port_list.size()-1)+1;
			mc.service_table_obj.port_list.add(this.service_port);
			System.out.println("PORT>>>"+this.service_port);
		}
		
		mc.service_table_obj.delete_service(this);
		
		if(service_thread!=null) {
			System.out.println("service_has_stop >>>"+message+this.service_port);
			JSONObject jsonObject=new JSONObject();
	    	try {
				jsonObject.put("mode", "AS-service");
				jsonObject.put("function", "AS-stop-service-id-back");
				jsonObject.put("node", mc.as_controller_obj.node_id);
				jsonObject.put("service_id", service_id);
				mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.as_back);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//重新发送
		mc.service_controller_obj.start_service_only_send_mqtt(service_model_obj,get_send_service_mes(),this);
		
		Runtime runtime = Runtime.getRuntime();
		BufferedReader br;
		try {
			System.out.println("RESTART>>>"+init_cmd+"  "+service_id+"  "+service_port+"  "+service_model_obj.config_json_path);
			br = new BufferedReader(new InputStreamReader(runtime.exec(init_cmd+"  "+service_id+"  "+service_port+"  "+service_model_obj.config_json_path).getInputStream()));
			//StringBuffer b = new StringBuffer();
            String line=null;
            StringBuffer b=new StringBuffer();
            while ((line=br.readLine())!=null) {
                b.append(line+"\n");
            }
            
            System.out.println(b.toString());
            service_has_stop(b.toString());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 服务停止
	 * @param message
	 */
	/*public void service_has_stop(String message) {
		System.out.println("service_has_stop");
		//更改port
		System.out.println("change service port");
		if(mc.service_table_obj.port_list.size()==0) {
			this.service_port=mc.service_table_obj.begin_service_port;
			mc.service_table_obj.port_list.add(this.service_port);
		}else {
			this.service_port=mc.service_table_obj.port_list.get(mc.service_table_obj.port_list.size()-1)+1;
			mc.service_table_obj.port_list.add(this.service_port);
			System.out.println("PORT>>>"+this.service_port);
		}
		
		mc.service_table_obj.delete_service(this);
		
		if(service_thread!=null) {
			System.out.println("service_has_stop >>>"+message+this.service_port);
			JSONObject jsonObject=new JSONObject();
	    	try {
				jsonObject.put("mode", "AS-service");
				jsonObject.put("function", "AS-stop-service-id-back");
				jsonObject.put("node", mc.as_controller_obj.node_id);
				jsonObject.put("service_id", service_id);
				mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.as_back);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//重新发送
		mc.service_controller_obj.start_service_only_send_mqtt(service_model_obj,get_send_service_mes(),this);
		if(service_model_obj.language.equals("php")) {
			Runtime runtime = Runtime.getRuntime();
			BufferedReader br;
			try {
				System.out.println("RESTART>>>"+init_cmd+"  "+service_id+"  "+service_port+"  "+service_model_obj.config_json_path);
				br = new BufferedReader(new InputStreamReader(runtime.exec(init_cmd+"  "+service_id+"  "+service_port+"  "+service_model_obj.config_json_path).getInputStream()));
				//StringBuffer b = new StringBuffer();
	            String line=null;
	            StringBuffer b=new StringBuffer();
	            while ((line=br.readLine())!=null) {
	                b.append(line+"\n");
	            }
	            
	            System.out.println(b.toString());
	            service_has_stop(b.toString());
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(service_model_obj.language.equals("java")) {
			//内置
			init_java_service init_java_service=new init_java_service(service_id, service_port+"", service_model_obj.config_json_path);
			boolean if_start_ok=init_java_service.start_service();
			System.out.println("JAVA SERVICE >>>"+if_start_ok);
			if(!if_start_ok) {
				service_has_stop("error");
			}
			
		}
	}*/
	
	/**
	 * 启动服务
	 */
	public void start_service() {
		System.out.println("start_service>>>"+service_model_obj.service_name+","+service_model_obj.version);
		if_service_start=true;
		service_thread.start();
	}
	
	/**
	 * 停止服务
	 */
	public void stop_service() {
		System.out.println("stop_service");
		if(service_thread!=null) {
			service_thread.stop();
		}
		service_thread=null;
	}
}