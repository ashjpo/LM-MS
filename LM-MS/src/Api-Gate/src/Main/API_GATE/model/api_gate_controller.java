package Main.API_GATE.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

/**
 * 用于对节点进行控制
 * @author mmy
 *
 */
public class api_gate_controller{
	public String gate_name;
	Thread hart_thread;
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	Timer timer;
	public api_gate_controller(Main_Context mc,String gate_name) {
		this.mc=mc;
		this.gate_name=gate_name;
		timer = new Timer();
		hart_thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						JSONObject jsonObject=new JSONObject();
			        	try {
							jsonObject.put("mode", "api-gate-hard");
							jsonObject.put("function", "Api-Gate-node-hart");
							jsonObject.put("api-gate-name", mc.gate_name);

							System.out.println("hart>>>"+jsonObject.toString());
							mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.api_gate_back);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, (int)Math.ceil(mc.config_obj.api_gate_hart_time/2)*1000, mc.config_obj.api_gate_hart_time*1000);
				
			}
		});
	}
	/**
	 * 关闭节点
	 */
	public void stop_api_gate() {
		//{"mode":"api-gate-hard","function":"Api-Gate-logout","api-gate-name":"x"}
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode", "api-gate-hard");
			jsonObject.put("function", "Api-Gate-node-logout");
			jsonObject.put("api-gate-name", mc.gate_name);
			mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.api_gate_back);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 重启节点
	 */
	public void reboot_api_gate() {
		//{"mode":"AS-hard","function":"AS-node-logout","node":"x"}
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode", "api-gate-hard");
			jsonObject.put("function", "Api-Gate-node-logout");
			jsonObject.put("api-gate-name", mc.gate_name);
			mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.api_gate_back);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 停止程序
	 */
	public void stop_program_api_gate() {
		System.exit(0);
	}
	
	/**
	 * 重启程序
	 */
	public void restart_program_api_gate() {
		//暂时
		Runtime run=Runtime.getRuntime(); 
		try { 
			int t=20;
			run.exec("shutdown -r -f -t "+t);
		} catch (IOException e) { 
			// TODO Auto-generated catch block e.printStackTrace(); }
		}
	}
	
	/**
	 * 启动
	 */
	public void api_gate_start() {
		//mqtt[TODO]
		//{"mode":"AS-hard","function":"AS-node-join","node":"x"}
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("mode", "api-gate-hard");
			jsonObject.put("function", "Api-Gate-node-join");
			jsonObject.put("api-gate-name", mc.gate_name);
			mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.api_gate_back);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.start_hart_message();
	}
	
	public void start_hart_message() {
		hart_thread.start();
	}
	
	public void stop_hart_message() {
		hart_thread.stop();
	}
	
	/**
	 * 发送节点的信息（用于展示）
	 */
	public void send_as_service_message_show() {
		String mod="api-gate-hard";
		String function="show-as-service-msg";
		JSONObject jsonObject=new JSONObject();
    	try {
    		jsonObject.put("mod", mod);
    		jsonObject.put("function", function);
    		
    		jsonObject.put("api-gate-name", this.mc.gate_name);
    		ArrayList<Service> temp_list=this.mc.service_table.get_service("*","*");
    		JSONArray t_Array=new JSONArray();
    		//System.out.println(function);
    		for(int i=0;i<temp_list.size();i++) {
    			JSONObject jobj=new JSONObject();
    			jobj.put("name",temp_list.get(i).service_name);
    			jobj.put("version",temp_list.get(i).version);
    			jobj.put("service_id",temp_list.get(i).service_id);
    			t_Array.put(jobj);
    		}
    		jsonObject.put("service-mes", t_Array.toString());
    		//System.out.println(jsonObject.toString());
			mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.message_show_back);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送节点状态数据（用于判断存在）
	 
	public void send_status_message() {
		//{"mode":"AS-hard","function":"AS-node-status","node":"x","svhashid":""}
    	JSONObject jsonObject=new JSONObject();
    	try {
			jsonObject.put("mode", "api-gate-hard");
			jsonObject.put("function", "Api-Gate-node-status");
			jsonObject.put("api-gate-name", this.mc.gate_name);
			
			
			ArrayList<Service> temp_array=mc.service_table_obj.get_service("*","*");
			String svhashid_str="";
			for(int i=0;i<temp_array.size();i++) {
				if(i==0) {
					svhashid_str=temp_array.get(i).service_model_obj.service_name;
				}else {
					svhashid_str=svhashid_str+"|"+temp_array.get(i).service_model_obj.service_name;
				}
			}
			jsonObject.put("svhashid", svhashid_str);
			mc.mqtt_obj.send_message(jsonObject.toString(),mc.config_obj.api_gate_back);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}