package Main.MCS;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import Context.Main_Context;
import Main.MCS.message.message_show;
import Main.MCS.message.logout.logout_main;
import Main.MCS.message.mcs_api_gate_back.mcs_api_gate_main;
import Main.MCS.message.mcs_as_back.mcs_as_main;
import Main.MCS.message.mcs_control.mcs_control_main;
import Main.MCS.monitor.monitor_as;
import Tools.mqtt.My_MqttCallBack;

public class MCS_Main{
	private String root_dir=null;
	Main_Context main_context=null;
	
	//处理消息对象
	mcs_control_main mcm_obj;
	mcs_as_main mam_obj;
	mcs_api_gate_main magm_obj;
	message_show message_show_obj;
	logout_main lm_obj;
	public MCS_Main(String root_dir) {
		//配置根目录
		this.root_dir=root_dir;
		this.main_context=new Main_Context(this.root_dir);
		
		mcm_obj=new mcs_control_main(this.main_context);
		mam_obj=new mcs_as_main(this.main_context);
		magm_obj=new mcs_api_gate_main(this.main_context);
		message_show_obj=new message_show(this.main_context);
		lm_obj=new logout_main(this.main_context);
		//接收消息
		this.main_context.mqtt_obj.set_callback(new My_MqttCallBack() {
			
			@Override
			public void messageArrived(String topicName, String message) {
				System.out.println("MCS>>>>>>"+message);
				if(topicName.equals(main_context.config_obj.mqtt_sub_mcs_control)) {
					mcm_obj.get_message(message);
				}else if(topicName.equals(main_context.config_obj.mqtt_sub_as_back)) {
					mam_obj.get_message(message);
				}else if(topicName.equals(main_context.config_obj.mqtt_sub_api_gate_back)) {
					magm_obj.get_message(message);
				}else if(topicName.equals(main_context.config_obj.mqtt_sub_message_show)) {
					message_show_obj.get_message(message);
				
				//}else if(topicName.equals(main_context.config_obj.mqtt_logout)) {
					//lm_obj.get_message(message);
				}
				
				
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
		String root_dir="";
		if(args.length==1) {
			root_dir="D:/MCS";
		}else {
			root_dir=args[1];
		}
		MCS_Main mcs_Main=new MCS_Main(root_dir);
	}
}