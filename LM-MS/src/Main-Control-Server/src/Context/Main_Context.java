package Context;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import Main.MCS.model.AS_table;
import Main.MCS.model.Api_gate_controller;
import Main.MCS.model.Api_gate_table;
import Main.MCS.model.Service;
import Main.MCS.model.Service_AS_controller;
import Main.MCS.model.Service_table;
import Main.MCS.monitor.monitor_api_gate;
import Main.MCS.monitor.monitor_as;
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
	
	//as_table,service_table,api_gate_table
	public AS_table as_table;
	public Service_table service_table;
	public Api_gate_table api_gate_table;
	
	//��ʼ��service as api-gate ������
	public Service_AS_controller service_AS_controller;
	public Api_gate_controller api_gate_controller;
	//monitor_as
	public monitor_as ma_obj;
	public monitor_api_gate mag_obj;
	
	public Main_Context(String root_dir) {
		this.root_dir=root_dir;
		//�����ļ���Ŀ¼
		String config_root_dir=this.root_dir+"/config";
		
		config_obj=new Config_Context(config_root_dir);
		
		//��ʼ��service as api-gate ������
		this.service_AS_controller=new Service_AS_controller(this);
		this.api_gate_controller=new Api_gate_controller(this);
		
		//��ʼ��mqtt
		this.init_mqtt();
		//��ʼ��as_table,service_table,api_gate_table
		this.init_tables();
		
		
		//��ʼ��service as monitor
		this.ma_obj=new monitor_as(this);
		this.ma_obj.start_monitor();
		
		//��ʼ��api-gate monitor
		this.mag_obj=new monitor_api_gate(this);
		this.mag_obj.start_monitor();
		
	}
	
	/**
	 * ��ʼ��as_table,service_table,api_gate_table
	 */
	public void init_tables() {
		as_table=new AS_table(this);
		service_table=new Service_table(this);
		api_gate_table=new Api_gate_table(this);
	}
	
	/**
	 * ��ʼ��mqtt
	 */
	public void init_mqtt() {
		String logout_message="";
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("node-type", "master");
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
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_mcs_control);
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_as_back);
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_api_gate_back);
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_message_show);
				mqtt_obj.add_subscribe(config_obj.mqtt_logout);
			}
		});
	
	}
}