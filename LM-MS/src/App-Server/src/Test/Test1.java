package Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import Tools.MMY_Config;
import Tools.mqtt.MyMqtt;
import Tools.mqtt.My_MqttCallBack;


public class Test1{
	public static void main(String[] args) {
		String mqtt_config_path="D:/MCS/mqtt.properties";
		MMY_Config mmy_config=new MMY_Config(mqtt_config_path);
		
		String host=mmy_config.get_string("host", "127.0.0.1");
		String passWord=mmy_config.get_string("userName", "user1");
		String userName=mmy_config.get_string("passWord", "pwd");
		String port=mmy_config.get_string("port", "1883");
		String disconnect_topic=mmy_config.get_string("disconnect_topic", "mcs_disconnect_topic");
		String disconnect_message=mmy_config.get_string("disconnect_message", "mcs_disconnect");
		
		
		MyMqtt mqtt=new MyMqtt(host, port, userName, passWord, disconnect_topic, disconnect_message);
		mqtt.connect();
		
		My_MqttCallBack my_MqttCallBack=new My_MqttCallBack() {
			
			@Override
			public void messageArrived(String topicName, MqttMessage message) {
				// TODO Auto-generated method stub
				System.out.println(topicName+"_"+message);
			}
			
			@Override
			public void connectionLost() {
				// TODO Auto-generated method stub
				System.out.print("connectionLost");
			}
			
			@Override
			public void connect() {
				// TODO Auto-generated method stub
				System.out.print("connect");
				mqtt.add_subscribe("test_order");
			}

			@Override
			public void connectError() {
				// TODO Auto-generated method stub
				System.out.print("connectError");
			}
		};
		
		
		
		mqtt.set_callback(my_MqttCallBack);
		
		System.out.println("A");
		
	}
}
