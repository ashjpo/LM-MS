package MCS_send_control;

import Context.Main_Context;
import Tools.mqtt.My_MqttCallBack;

public class show_message{
	private String root_dir=null;
	Main_Context main_context=null;
	public show_message(String root_dir) {
		//配置根目录
		this.root_dir=root_dir;
		this.main_context=new Main_Context(this.root_dir);
		
		//接收消息
				this.main_context.mqtt_obj.set_callback(new My_MqttCallBack() {
					
					@Override
					public void messageArrived(String topicName, String message) {
						System.out.println(topicName+">>>"+message);
					}
					
					@Override
					public void connectionLost() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void connectError() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void connect() {
						// TODO Auto-generated method stub
						
					}
				});
	}
	
	
	public static void main(String[] args) {
		String root_dir="D:/service_control";
		show_message mcs_Main=new show_message(root_dir);
	}
}