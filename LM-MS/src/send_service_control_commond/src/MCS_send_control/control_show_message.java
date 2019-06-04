package MCS_send_control;

import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;
import Tools.mqtt.My_MqttCallBack;

public class control_show_message{
	private String root_dir=null;
	Main_Context main_context=null;
	public control_show_message(String root_dir) {
		//ÅäÖÃ¸ùÄ¿Â¼
		this.root_dir=root_dir;
		this.main_context=new Main_Context(this.root_dir);
		
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
				show_service_as_mcs("*");
				show_api_gate_mcs("*");
				show_service_as_api_gate("*");
				
			}
		});
	}
	
	/**
	 *
	 */
	public void show_service_as_mcs(String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","MCS");
			jsonObject.put("function","show-as-service-msg");
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_mcs_message_show);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 */
	public void show_api_gate_mcs(String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","MCS");
			jsonObject.put("function","show-api-gate-msg");
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_mcs_message_show);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 */
	public void show_service_as(String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","AS");
			jsonObject.put("function","show-as-service-msg");
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_as_message_show);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 */
	public void show_service_as_api_gate(String api_name) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","Api-Gate");
			jsonObject.put("function","show-as-service-msg");
			jsonObject.put("api-gate-name",api_name);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_api_gate_message_show);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String root_dir="D:/service_control";
		control_show_message mcs_Main=new control_show_message(root_dir);
		
		
		
	}
}