package Context;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import Main.AS.model.as_controller;
import Main.AS.model.service_controller;
import Main.AS.model.service_model;
import Main.AS.model.service_model_rpc;
import Main.AS.model.service_model_table;
import Main.AS.model.service_table;
import Tools.MMY_Json;
import Tools.mqtt.MyMqtt;
import Tools.mqtt.My_MqttCallBack;

public class Main_Context{
	public boolean if_debug=false;
	public MyMqtt mqtt_obj=null;
	boolean mqtt_if_connecting=false;
	boolean mqtt_if_connected=false;
	int mqtt_connect_times=0;
	private String root_dir=null;
	public Config_Context config_obj=null;
	
	public as_controller as_controller_obj;
	public service_controller service_controller_obj;
	
	public service_model_table service_model_table_obj;
	public service_table service_table_obj;
	public String node_id;
	
	public Main_Context(String root_dir) {
		this.root_dir=root_dir;
		//配置文件根目录
		String config_root_dir=this.root_dir+"/config";
		
		config_obj=new Config_Context(config_root_dir);
		
		//节点id
		node_id="node_"+System.currentTimeMillis()+"_"+new Random().nextInt(100);
		
		this.as_controller_obj=new as_controller(this,node_id);
		
		//发送心跳数据
		this.service_controller_obj=new service_controller(this);
		
		this.service_model_table_obj=new service_model_table();
		this.service_table_obj=new service_table();
		
		this.read_all_sevice_model();
		
		//初始化mqtt
		this.init_mqtt();
		
		
		
		
	}
	
	/**
	 * 读取所有service model
	 * 
	 */
	public void read_all_sevice_model() {
		MMY_Json mmy_Json=new MMY_Json();
		String service_dir=this.root_dir+"/service";
		File dir = new File(service_dir);
	    File[] files = dir.listFiles();
	    FileFilter fileFilter = new FileFilter() {
	         public boolean accept(File file) {
	            return file.isDirectory();
	         }
	      };
	    files = dir.listFiles(fileFilter);
	    
	    ArrayList<String> svhashid_list=new ArrayList<>();
	    for (int i=0; i< files.length; i++) {
            File filename = files[i];
            //System.out.println(filename.toString());
            String each_service_config_dir=filename.toString()+"/config/service.json";
            System.out.println(each_service_config_dir);
            File service_config_file =new File(each_service_config_dir);
            if(service_config_file.exists()) {
            	String json_str=mmy_Json.readJsonFile(each_service_config_dir);
            	JSONObject service_json;
            	String service_name="";
            	String version="";
				try {
					service_json = new JSONObject(json_str);
					service_name=service_json.getString("name");
					version=service_json.getString("version");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	if(!svhashid_list.contains(service_name+version)) {
            		svhashid_list.add(service_name+version);
            	}else {
            		System.out.println("service same name error");
            		System.exit(0);
            	}
            	service_model service_model_obj=new service_model(each_service_config_dir);
            	this.service_model_table_obj.add_service_model(service_model_obj);
            }
        }


	}
	
	/**
	 * 初始化mqtt
	 */
	public void init_mqtt() {
		String logout_message="";
		JSONObject jsonObject=new JSONObject();
		try {
			//{"mode":"AS-hard","function":"AS-node-logout","node":"x"}
			jsonObject.put("mode", "AS-hard");
			jsonObject.put("function", "AS-node-logout");
			jsonObject.put("node", this.as_controller_obj.node_id);
			logout_message=jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mqtt_obj=new MyMqtt(config_obj.mqtt_host, config_obj.mqtt_port, config_obj.mqtt_userName, config_obj.mqtt_passWord, config_obj.mqtt_disconnect_topic, logout_message);
		mqtt_if_connecting=true;
		mqtt_obj.connect();
		mqtt_connect_times=mqtt_connect_times+1;
		mqtt_obj.set_callback(new My_MqttCallBack() {
			
			@Override
			public void messageArrived(String topicName, String message) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectionLost() {
				if(!if_debug) {
					System.out.println("mqtt lost connect");
				}
				// TODO Auto-generated method stub
				if(mqtt_if_connected==false && mqtt_if_connecting==false && mqtt_connect_times<=config_obj.mqtt_reconnect_times) {
					mqtt_if_connecting=true;
					mqtt_obj.connect();
					mqtt_connect_times=mqtt_connect_times+1;
				}
			}
			
			@Override
			public void connectError() {
				// TODO Auto-generated method stub
				mqtt_if_connecting=false;
				if(mqtt_if_connected==false && mqtt_if_connecting==false && mqtt_connect_times<=config_obj.mqtt_reconnect_times) {
					mqtt_if_connecting=true;
					mqtt_obj.connect();
					mqtt_connect_times=mqtt_connect_times+1;
				}
			}
			
			@Override
			public void connect() {
				if(!if_debug) {
					System.out.println("mqtt connect");
				}
				mqtt_obj.send_message("","test_message");
				
				mqtt_if_connected=true;
				mqtt_if_connecting=false;
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_as_control);
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_message_show);
				mqtt_obj.add_subscribe(config_obj.mqtt_logout);
				
				//启动节点
				as_controller_obj.as_start();
			}
		});
	
	}
}