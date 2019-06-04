package Main.MCS.message.mcs_control;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_control_AS{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public mcs_control_AS(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("AS-show-service")) {	//�鿴���з���
					//{"mode":"AS","function":"AS-show-service"}
					
				}else if(function.equals("AS-start-service")) {	//����ĳ���ڵ��ĳ������ĳ��hashid��
					//{"mode":"AS","function":"AS-start-service","service":"x","version":"*","node":"*","num":1}
					String service=json_obj.getString("service");
					String version=json_obj.getString("version");
					String node=json_obj.getString("node");
					int num=1;
					try {
						num=json_obj.getInt("num");
					} catch (Exception e) {
						num=1;
					}
					
					JSONObject send_json=new JSONObject();
					send_json.put("node", node);
					send_json.put("mode", "AS");
					send_json.put("function", function);
					send_json.put("service", service);
					send_json.put("version", version);
					send_json.put("num", num);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_as_control);
					
				}else if(function.equals("AS-stop-service")) {	//ֹͣĳ���ڵ�ķ���
					//{"mode":"AS","function":"AS-stop-service","service":"x","version":"*","node":"*","num":-1}
					String service=json_obj.getString("service");
					String version=json_obj.getString("version");
					String node=json_obj.getString("node");
					int num=1;
					try {
						num=json_obj.getInt("num");
					} catch (Exception e) {
						num=1;
					}
					
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "AS");
					send_json.put("node", node);
					send_json.put("function", function);
					send_json.put("service", service);
					send_json.put("version", version);
					send_json.put("num", num);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_as_control);
					
				}else if(function.equals("AS-stop-service-id")) {	//ֹͣĳ���ڵ�id�ķ���
					//{"mode":"AS","function":"AS-stop-service-id","service_id":"x1","node":"x"}}
					String service_id=json_obj.getString("service_id");
					String node=json_obj.getString("node");
					int num=-1;
					try {
						num=json_obj.getInt("num");
					} catch (Exception e) {
						num=-1;
					}
					
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "AS");
					send_json.put("node", node);
					send_json.put("function", function);
					send_json.put("service_id", service_id);
					send_json.put("num", num);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_as_control);
					
				}else if(function.equals("AS-shutdown-node")) {	//�ڵ�ػ�
					//{"mode":"AS","function":"AS-shutdown-node","node":"*"}
					String node=json_obj.getString("node");
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "AS-hard");
					send_json.put("node", node);
					send_json.put("function", function);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_as_control);
					
				}else if(function.equals("AS-reboot-node")) {	//�ڵ�����
					//{"mode":"AS","function":"AS-reboot-node","node":"*"}
					String node=json_obj.getString("node");
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "AS-hard");
					send_json.put("node", node);
					send_json.put("function", function);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_as_control);
				
				}else if(function.equals("AS-stop-node")) {	//�ڵ�رճ���
					//{"mode":"AS","function":"AS-stop-node","node":"*"}
					String node=json_obj.getString("node");
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "AS-hard");
					send_json.put("node", node);
					send_json.put("function", function);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_as_control);
					
				}else if(function.equals("AS-restart-node")) {	//�ڵ�رճ���
					//{"mode":"AS","function":"AS-stop-node","node":"*"}
					String node=json_obj.getString("node");
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "AS-hard");
					send_json.put("node", node);
					send_json.put("function", function);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_as_control);
					
				}else if(function.equals("AS-node-status")) {	//�ڵ�״̬��ѯ
					//{"mode":"AS","function":"AS-node-status","node":"*"}
					String node=json_obj.getString("node");
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "AS-hard");
					send_json.put("node", node);
					send_json.put("function", function);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_as_control);
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}