package Main.MCS.monitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import Context.Main_Context;
import Main.MCS.model.AS;
import Main.MCS.model.Api_gate;
import Main.MCS.model.Service;
import Tools.MMY_Json;

public class monitor_api_gate{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	Thread monitor_thread;
	Timer timer;
	public monitor_api_gate(Main_Context mc) {
		this.mc=mc;
		timer = new Timer();
		monitor_thread = new Thread(new Runnable(){

			@Override
			public void run() {
				timer.schedule(new TimerTask() {
			        public void run() {
			            monitor_task();
			        }
				}, (int)Math.ceil(mc.config_obj.api_gate_monitor_hart_time/2)*1000, mc.config_obj.api_gate_monitor_hart_time*1000);

			}
			
		});
	}
	
	/**
	 * 获取api-gate发来的心跳包
	 * @param message
	 */
	public void get_api_gate_hart(String message) {
		long now_time=System.currentTimeMillis();
		//{"mode":"api-gate-hard","function":"Api-Gate-node-hart","api-gate-name":"x"}
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String api_gate_name = json_obj.getString("api-gate-name");
			this.mc.api_gate_table.get_api_gate_by_name(api_gate_name).last_hart_time=now_time;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 获取api-gate发来的心跳包
	 * @param message
	 */
	public void get_api_gate_status(String message) {
		long now_time=System.currentTimeMillis();
		//{"mode":"api-gate-hard","function":"Api-Gate-node-status","api-gate-name":"x","status":""}
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String api_gate_name = json_obj.getString("api-gate-name");
			this.mc.api_gate_table.get_api_gate_by_name(api_gate_name).last_hart_time=now_time;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 监控任务
	 */
	private void monitor_task() {
		LinkedList<Api_gate> hart_outtime_apigate_list=new LinkedList<>();
		ArrayList<Api_gate> temp_apigate_list=mc.api_gate_table.get_api_gate();
		for(int i=0;i<temp_apigate_list.size();i++) {
			Api_gate temp_api_gate=temp_apigate_list.get(i);
			long now_time=System.currentTimeMillis();
			long apigate_last_hart_time=temp_api_gate.last_hart_time;
			long time_step=now_time-apigate_last_hart_time;
			//监控节点
			if(time_step>mc.config_obj.api_gate_hart_outline_time*1000) {
				hart_outtime_apigate_list.add(temp_api_gate);
			}
			
		}
		
		this.outline_api_gate(hart_outtime_apigate_list);
	}
	
	/**
	 * 将节点下线
	 * @param as_list
	 */
	private void outline_api_gate(LinkedList<Api_gate> api_gate_list) {
		for(int i=0;i<api_gate_list.size();i++) {
			this.mc.api_gate_controller.api_gate_logout(api_gate_list.get(i));
		}
	}
	
	/**
	 * 启动线程定时监控心跳包
	 */
	public void start_monitor() {
		monitor_thread.start();
	}
	
	/**
	 * 停止线程定时监控心跳包
	 */
	public void stop_monitor() {
		monitor_thread.stop();
	}
}