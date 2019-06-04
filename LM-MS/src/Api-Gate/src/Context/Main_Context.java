package Context;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import Main.API_GATE.functions.flow.TokenBucket;
import Main.API_GATE.message.api_gate_control_main;
import Main.API_GATE.model.AS_table;
import Main.API_GATE.model.Service_AS_controller;
import Main.API_GATE.model.Service_table;
import Main.API_GATE.model.api_gate_controller;
import Main.API_GATE.work.get_data;
import Main.API_GATE.work.service_dealer;
import Main.API_GATE.work.service_dispatch;
import Main.API_GATE.work.service_dispatch_table;
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
	
	//as_table,service_table,api_gate_table
	public AS_table as_table;
	public Service_table service_table;
	
	public api_gate_controller api_gate_controller_obj;
	
	public String gate_name;
	
	//��ʼ��service as api-gate ������
	public Service_AS_controller service_AS_controller;
	
	//��ͨ������
	public TokenBucket main_tb;
	
	//��������
	public service_dispatch service_dispatch_obj;
	//���ȱ�
	public service_dispatch_table service_dispatch_table_obj;
	
	public get_data get_data_obj;
	
	public Main_Context(String root_dir) {
		this.root_dir=root_dir;
		//�����ļ���Ŀ¼
		String config_root_dir=this.root_dir+"/config";
		
		config_obj=new Config_Context(config_root_dir);
		
		//��ʼ��service as api-gate ������
		this.service_AS_controller=new Service_AS_controller(this);
		
		
		
		
		//�ڵ�id
		gate_name="api_gate_"+System.currentTimeMillis()+"_"+new Random().nextInt(100);
		this.api_gate_controller_obj=new api_gate_controller(this,gate_name);
		
		
		//��ʼ��mqtt
		this.init_mqtt();
		//��ʼ��as_table,service_table
		this.init_tables();
		
		this.init_server();

		
		
	}
	
	public void init_server() {
		this.main_tb=TokenBucket.newBuilder().avgFlowRate(config_obj.api_gate_average_flow).maxFlowRate(config_obj.api_gate_max_flow).build();
		this.service_dispatch_table_obj=new service_dispatch_table(this);
		this.service_dispatch_obj=new service_dispatch(this);
		this.get_data_obj=new get_data();
	}
	
	/**
	 * ��ʼ��as_table,service_table,api_gate_table
	 */
	public void init_tables() {
		as_table=new AS_table(this);
		service_table=new Service_table(this);
	}
	
	/**
	 * ��ʼ��mqtt
	 */
	public void init_mqtt() {

		String logout_message="";
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode", "api-gate-hard");
			jsonObject.put("function", "Api-Gate-node-logout");
			jsonObject.put("api-gate-name", this.gate_name);
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
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_api_gate_control);
				mqtt_obj.add_subscribe(config_obj.mqtt_sub_message_show);
				mqtt_obj.add_subscribe(config_obj.mqtt_logout);

				
				api_gate_controller_obj.api_gate_start();
			}
		});

	}
}