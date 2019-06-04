package Main.AS.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.dgc.Lease;
import java.util.Random;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;

public class service{
	Main_Context mc;
	public String call_type;
	//public service_model_rpc service_model_obj;
	public String service_id;
	public int service_port;
	public long start_time;
	public service_model service_model_obj;
	//初始化文件路径
	String init_cmd="";
	public service(service_model service_model_obj,Main_Context mc) {
		this.mc=mc;
		
		this.service_model_obj=service_model_obj;
		this.call_type=service_model_obj.call_type;
		this.service_id="service_"+System.currentTimeMillis()+"_"+new Random().nextInt(100);
		if(mc.service_table_obj.port_list.size()==0) {
			this.service_port=mc.service_table_obj.begin_service_port;
		}else {
			this.service_port=mc.service_table_obj.port_list.get(mc.service_table_obj.port_list.size()-1)+1;
		}
		mc.service_table_obj.set_service_port_list(this.service_port);
		
		this.start_time=System.currentTimeMillis();
		
		
		
	}
	
	/**
	 * 获取发送给mcs的service-mes数组的一个元素
	 * @return
	 */
	public JSONObject get_send_service_mes() {
		JSONObject temp_obj=new JSONObject();
		try {
			temp_obj.put("service_id", service_id);
			temp_obj.put("call_type", call_type);
			temp_obj.put("service_name", service_model_obj.service_name);
			temp_obj.put("service_type", service_model_obj.service_type);
			temp_obj.put("version", service_model_obj.version);
			temp_obj.put("service_host", service_model_obj.service_host);
			if(call_type.equals("rpc")) {
				temp_obj.put("service_url", service_model_obj.service_url);
			}
			temp_obj.put("service_port", this.service_port);
			temp_obj.put("version_code", service_model_obj.version_code);
			temp_obj.put("language", service_model_obj.language);
			temp_obj.put("api-gate", service_model_obj.api_gate);
			temp_obj.put("service_mes", service_model_obj.service_mes);
			temp_obj.put("functions", service_model_obj.fun_array);
			return temp_obj;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
}