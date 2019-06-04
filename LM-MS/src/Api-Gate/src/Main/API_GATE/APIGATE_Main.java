package Main.API_GATE;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import Context.Main_Context;
import Main.API_GATE.message.api_gate_control_main;
import Main.API_GATE.message.message_show;
import Main.API_GATE.service.Server;
import Tools.mqtt.My_MqttCallBack;

public class APIGATE_Main{
	private String root_dir=null;
	Main_Context main_context=null;
	
	//处理消息对象
	api_gate_control_main api_control_obj;
	message_show message_show_obj;
	public APIGATE_Main(String root_dir) {
		//配置根目录
		this.root_dir=root_dir;
		this.main_context=new Main_Context(this.root_dir);
		
		api_control_obj=new api_gate_control_main(this.main_context);
		message_show_obj=new message_show(this.main_context);
		
		this.start_server();
		
		
		//接收消息
		this.main_context.mqtt_obj.set_callback(new My_MqttCallBack() {
			
			@Override
			public void messageArrived(String topicName, String message) {
				//System.out.println(message);
				if(topicName.equals(main_context.config_obj.mqtt_sub_api_gate_control)) {
					api_control_obj.get_message(message);
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
	
	public void start_server() {
		Thread hart_thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				Server server=new Server(main_context);
				server.run();
			}
		});
		
		hart_thread.start();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		String root_dir="";
		if(args.length==1) {
			root_dir="D:/Api_Gate";
		}else {
			root_dir=args[1];
		}
		APIGATE_Main mcs_Main=new APIGATE_Main(root_dir);
	}
}