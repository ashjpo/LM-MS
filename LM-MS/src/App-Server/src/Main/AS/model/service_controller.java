package Main.AS.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;

/**
 * 用于控制服务的启动和关闭
 * @author mmy
 *
 */
public class service_controller{
	Main_Context mc;
	public service_controller(Main_Context mc) {
		this.mc=mc;
	}
	
	/**
	 * 启动service
	 */
	public void start_service(String service_name,String version) {
		ArrayList<service_model> temp_list=this.mc.service_model_table_obj.get_service_model(service_name, version);
		JSONArray service_mes=new JSONArray();
		for(int i=0;i<temp_list.size();i++) {
			service_model service_model_obj=temp_list.get(i);
			//System.out.println(service_model_obj.call_type);
			if(service_model_obj.call_type.equals("rpc")) {
				service_rpc service_obj=new service_rpc(service_model_obj, this.mc);
				this.mc.service_table_obj.add_service(service_obj);
				service_obj.start_service();
				if(service_obj.get_send_service_mes()!=null) {
					service_mes.put(service_obj.get_send_service_mes());
				}
			}else if(service_model_obj.call_type.equals("http")) {
				service_http service_obj=new service_http(service_model_obj, this.mc);
				this.mc.service_table_obj.add_service(service_obj);
				if(service_obj.get_send_service_mes()!=null) {
					service_mes.put(service_obj.get_send_service_mes());
				}
			}
		}
		
		//System.out.println(service_name+version+service_mes.toString());
		if(service_mes.length()!=0) {
			//{"mode":"AS-service","function":"AS-start-service-back","node":"x","service-mes":[]}
			JSONObject jsonObject=new JSONObject();
	    	try {
				jsonObject.put("mode", "AS-service");
				jsonObject.put("function", "AS-start-service-back");
				jsonObject.put("node", mc.as_controller_obj.node_id);
				jsonObject.put("service-mes", service_mes);
				//System.out.println(jsonObject.toString());
				mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.as_back);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 用于端口冲突时候更改端口后启动服务发送消息
	 */
	public void start_service_only_send_mqtt(service_model service_model_obj,JSONObject get_service_mes,service service) {
		JSONArray service_mes=new JSONArray();
		//System.out.println(service_model_obj.call_type);
		if(service_model_obj.call_type.equals("rpc")) {
			this.mc.service_table_obj.add_service(service);
			if(get_service_mes!=null) {
				service_mes.put(get_service_mes);
			}
		}
		//System.out.println(service_name+version+service_mes.toString());
		if(service_mes.length()!=0) {
			//{"mode":"AS-service","function":"AS-start-service-back","node":"x","service-mes":[]}
			JSONObject jsonObject=new JSONObject();
	    	try {
				jsonObject.put("mode", "AS-service");
				jsonObject.put("function", "AS-start-service-back");
				jsonObject.put("node", mc.as_controller_obj.node_id);
				jsonObject.put("service-mes", service_mes);
				//System.out.println(jsonObject.toString());
				mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.as_back);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 停止service
	 */
	public void stop_service_by_id(String service_id) {
		service_rpc service_obj=(service_rpc) this.mc.service_table_obj.get_service_by_id(service_id);
		if(service_obj!=null) {
			this.mc.service_table_obj.delete_service(service_obj);
			if(service_obj.call_type.equals("rpc")) {
				service_obj.stop_service();
			}
			//{"mode":"AS-service","function":"AS-stop-service-id-back","node":"x","service_id":"x"}
			JSONObject jsonObject=new JSONObject();
	    	try {
				jsonObject.put("mode", "AS-service");
				jsonObject.put("function", "AS-stop-service-id-back");
				jsonObject.put("node", mc.as_controller_obj.node_id);
				jsonObject.put("service_id", service_obj.service_id);
				mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.as_back);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			service_obj=null;
			
		}
	}
	
	/**
	 * 停止service
	 */
	public void stop_service(String service_name,String version) {
		ArrayList<service> temp_list=this.mc.service_table_obj.get_service(service_name, version);
		for(int i=0;i<temp_list.size();i++) {
			if(temp_list.get(i).call_type.equals("rpc")) {
				service_rpc temp=(service_rpc)temp_list.get(i);
				temp.stop_service();
			}
		}
		
		this.mc.service_table_obj.delete_service_list(temp_list);
		
		
		//{"mode":"AS-service","function":"AS-stop-service-back","node":"x","svhashid":""}
		JSONObject jsonObject=new JSONObject();
    	try {
			jsonObject.put("mode", "AS-service");
			jsonObject.put("function", "AS-stop-service-back");
			jsonObject.put("node", mc.as_controller_obj.node_id);
			jsonObject.put("service", service_name);
			jsonObject.put("version", version);
			jsonObject.put("svhashid", service_name+version);
			mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.as_back);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}