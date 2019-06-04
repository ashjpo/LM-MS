package MCS_send_control;

import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;
import Tools.mqtt.My_MqttCallBack;

public class control_service_as{
	private String root_dir=null;
	Main_Context main_context=null;
	public control_service_as(String root_dir) {
		//配置根目录
		this.root_dir=root_dir;
		this.main_context=new Main_Context(this.root_dir);
		
		this.main_context.mqtt_obj.set_callback(new My_MqttCallBack() {
			
			@Override
			public void messageArrived(String topicName, String message) {
				System.out.println(message);
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
				//启动服务
				start_service("serviceB|serviceD|serviceE", "*", "*");
				//stop_service("serviceA", "*", "*");
				//stop_service_id("service_1559206182267_20","*");
			}
		});
	}
	
	/**
	 * 启动服务
	 * 
	 *  {"mode":"AS","function":"AS-start-service","service":"*","version":"*","node":"*"}
	 * 	{"mode":"AS","function":"AS-start-service","service":"serviceA","version":"*","node":"*"}
	 * 	{"mode":"AS","function":"AS-start-service","service":"serviceA","version":"v1.0","node":"*"}
	 * 	{"mode":"AS","function":"AS-start-service","service":"serviceA","version":"*","node":"node1"}
	 * 
	 * 
	 */
	public void start_service(String service_name,String service_version,String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","AS");
			jsonObject.put("function","AS-start-service");
			jsonObject.put("service",service_name);
			jsonObject.put("version",service_version);
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_mcs_control);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 停止服务
	 * 
	 *  {"mode":"AS","function":"AS-stop-service","service":"*","version":"*","node":"*"}
	 * 
	 */
	public void stop_service(String service_name,String service_version,String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","AS");
			jsonObject.put("function","AS-stop-service");
			jsonObject.put("service",service_name);
			jsonObject.put("version",service_version);
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_mcs_control);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 停止服务(service_id)
	 * 
	 *  {"mode":"AS","function":"AS-stop-service-id","service_id":"xxx","node":"*"}
	 * 
	 */
	public void stop_service_id(String service_id,String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","AS");
			jsonObject.put("function","AS-stop-service-id");
			jsonObject.put("service_id",service_id);
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_mcs_control);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 节点关机
	 * {"mode":"AS","function":"AS-shutdown-node","node":"*"}
	 */
	public void shutdown_as(String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","AS");
			jsonObject.put("function","AS-shutdown-node");
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_mcs_control);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 节点重启
	 * {"mode":"AS","function":"AS-reboot-node","node":"*"}
	 */
	public void reboot_as(String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","AS");
			jsonObject.put("function","AS-reboot-node");
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_mcs_control);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 节点程序关闭
	 * {"mode":"AS","function":"AS-stop-node","node":"*"}
	 */
	public void stop_as_program(String node) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode","AS");
			jsonObject.put("function","AS-stop-node");
			jsonObject.put("node",node);
			this.main_context.mqtt_obj.send_message(jsonObject.toString(),this.main_context.config_obj.mqtt_mcs_control);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String root_dir="D:/service_control";
		control_service_as mcs_Main=new control_service_as(root_dir);
		
		
		
	}
}