package Context;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	
	
	public Main_Context(String root_dir) {
		this.root_dir=root_dir;
		//配置文件根目录
		String config_root_dir=this.root_dir+"/config";
		
		config_obj=new Config_Context(config_root_dir);
		
		
		//初始化mqtt
		this.init_mqtt();
		

		
		
	}
	
	/**
	 * 初始化mqtt
	 */
	public void init_mqtt() {
		String logout_message="";
		JSONObject jsonObject=new JSONObject();
		logout_message="";
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
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_message_show_back);
			}
		});
	
	}
}