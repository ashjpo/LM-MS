package Main.AS;


import org.eclipse.paho.client.mqttv3.MqttMessage;

import Context.Main_Context;
import Main.AS.message.as_control_main;
import Main.AS.message.logout_main;
import Main.AS.message.message_show;
import Tools.mqtt.My_MqttCallBack;

public class AS_Main{
	private String root_dir=null;
	Main_Context main_context=null;
	logout_main lm_obj;
	
	//处理消息对象
	as_control_main as_control_main_obj;
	message_show message_show_obj;
	public AS_Main(String root_dir) {
		//配置根目录
		this.root_dir=root_dir;
		this.main_context=new Main_Context(this.root_dir);
		as_control_main_obj=new as_control_main(this.main_context);
		message_show_obj=new message_show(this.main_context);
		lm_obj=new logout_main(this.main_context);
		//接收消息
		this.main_context.mqtt_obj.set_callback(new My_MqttCallBack() {
			
			@Override
			public void messageArrived(String topicName, String message) {
				//System.out.println(topicName+">>>"+message);
				if(topicName.equals(main_context.config_obj.mqtt_sub_as_control)) {
					as_control_main_obj.get_message(message);
				//}else if(topicName.equals(main_context.config_obj.mqtt_logout)) {
					//lm_obj.get_message(message);
				}else if(topicName.equals(main_context.config_obj.mqtt_sub_message_show)) {
					message_show_obj.get_message(message);
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
			root_dir="D:/mic_service";
		}else {
			root_dir=args[1];
		}
		AS_Main mcs_Main=new AS_Main(root_dir);
	}
}