package Main.MCS.message.mcs_control;


import org.json.JSONArray;
import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_control_api_gate{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public mcs_control_api_gate(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("Api-Gate-show")) {	//查看现有api-gate
					//{"mode":"api-gate","function":"Api-Gate-show"}
					
				}else if(function.equals("Api-Gate-close-api-gate")) {	//停止api网关接收请求
					//{"mode":"api-gate","function":"Api-Gate-close-api-gate","gate-name":"*","stop-time":"5"}
					String gate_name=json_obj.getString("gate-name");
					int stop_time=-1;
					try {
						stop_time=json_obj.getInt("stop-time");
					} catch (Exception e) {
						stop_time=-1;
					}
					
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "api-gate-hard");
					send_json.put("function", function);
					send_json.put("gate-name", gate_name);
					send_json.put("stop_time", stop_time);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_api_gate_control);
				}else if(function.equals("Api-Gate-open-api-gate")) {	//开放api网关接收请求
					//{"mode":"api-gate","function":"Api-Gate-open-api-gate","gate-name":"*","stop-time":"5"}
					String gate_name=json_obj.getString("gate-name");
					int stop_time=-1;
					try {
						stop_time=json_obj.getInt("stop-time");
					} catch (Exception e) {
						stop_time=-1;
					}
					
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "api-gate-hard");
					send_json.put("function", function);
					send_json.put("gate-name", gate_name);
					send_json.put("stop_time", stop_time);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_api_gate_control);
					
				}else if(function.equals("Api-Gate-status")) {	//网关状态查询
					//{"mode":"api-gate","function":"Api-Gate-status","gate-name":"*"}
					String gate_name=json_obj.getString("gate-name");
					
					JSONObject send_json=new JSONObject();
					send_json.put("mode", "api-gate-hard");
					send_json.put("function", function);
					send_json.put("gate-name", gate_name);
					this.mc.mqtt_obj.send_message(send_json.toString(), this.mc.config_obj.mqtt_api_gate_control);
					
				}
				
				
				
				/*else if(function.equals("Api-Gate-regist-service")) {	//注册服务
					//{"mode":"api-gate","function":"Api-Gate-regist-service","gate-name":"*","service-mes":[]}
					String gate_name=json_obj.getString("gate-name");
					JSONArray service_mes=json_obj.getJSONArray("service-mes");

					JSONObject send_json=new JSONObject();
					send_json.put("mode", mode);
					send_json.put("function", function);
					send_json.put("gate-name", gate_name);
					send_json.put("service-mes", service_mes);
					String topic="api-control";
					this.mc.mqtt_obj.send_message(send_json.toString(), topic);
					
				}else if(function.equals("Api-Gate-logout-service")) {	//注销服务
					//{"mode":"api-gate","function":"Api-Gate-logout-service","gate-name":"*","service-id":[]}
					String gate_name=json_obj.getString("gate-name");
					JSONArray service_mes=json_obj.getJSONArray("service-mes");

					JSONObject send_json=new JSONObject();
					send_json.put("mode", mode);
					send_json.put("function", function);
					send_json.put("gate-name", gate_name);
					send_json.put("service-mes", service_mes);
					String topic="api-control";
					this.mc.mqtt_obj.send_message(send_json.toString(), topic);
					
				*/
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}