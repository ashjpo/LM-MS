package Main.MCS.monitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import Context.Main_Context;
import Main.MCS.model.AS;
import Main.MCS.model.AS_table;
import Main.MCS.model.Service;
import Main.MCS.model.Service_table;
import Tools.MMY_Json;

/**
 * 用于监控as和service的状态
 * @author mmy
 *
 */

public class monitor_as{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	Thread monitor_thread;
	Timer timer;
	public monitor_as(Main_Context mc) {
		this.mc=mc;
		timer = new Timer();
		monitor_thread = new Thread(new Runnable(){

			@Override
			public void run() {
				timer.schedule(new TimerTask() {
			        public void run() {
			            monitor_task();
			        }
				},  (int)Math.ceil(mc.config_obj.as_monitor_hart_time/2)*1000,mc.config_obj.as_monitor_hart_time*1000);
				//}, 0 , (int)Math.ceil(mc.config_obj.as_monitor_hart_time/2)*1000);
			}
			
		});
	}
	
	/**
	 * 获取AS发来的心跳包
	 * @param message
	 */
	public void get_as_hart(String message) {
		long now_time=System.currentTimeMillis();
		//{"mode":"AS-hard","function":"AS-node-hart","node":"x","service_id":""}
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			//String service = json_obj.getString("service");
			String node_id = json_obj.getString("node");
			String service_id = json_obj.getString("service_id");
			//System.out.println(mc.as_table.get_all_as().toString());
			for(int i=0;i<mc.as_table.get_all_as().size();i++) {
				if(mc.as_table.get_all_as().get(i).node_id.equals(node_id)) {
					//System.out.println("update hart time "+now_time);
					mc.as_table.get_all_as().get(i).last_hart_time=now_time;
				}
			}
			if(service_id.equals("*")) {
				AS temp_as=mc.as_table.get_as_by_id(node_id);
				temp_as.last_hart_time=now_time;
				ArrayList<Service> service_list=temp_as.get_all_service();
				for(int i=0;i<service_list.size();i++) {
					service_list.get(i).last_hart_time=now_time;
				}
			}else{
				AS temp_as=mc.as_table.get_as_by_id(node_id);
				temp_as.last_hart_time=now_time;
				String[] service_id_array=service_id.split("\\|");
				for(int i=0;i<service_id_array.length;i++) {
					ArrayList<Service> service_list=temp_as.get_all_service_by_id(service_id_array[i]);
					for(int j=0;j<service_list.size();j++) {
						service_list.get(j).last_hart_time=now_time;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 获取AS发来的状态返回数据
	 * @param message
	 */
	public void get_as_status(String message) {
		//{"mode":"AS-hard","function":"AS-node-status","node":"x","svhashid":"","status":""}
		long now_time=System.currentTimeMillis();
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String service = json_obj.getString("service");
			String node_id = json_obj.getString("node");
			String svhashid = json_obj.getString("svhashid");
			for(int i=0;i<mc.as_table.get_all_as().size();i++) {
				if(mc.as_table.get_all_as().get(i).node_id.equals(node_id)) {
					//System.out.println("update hart time "+now_time);
					mc.as_table.get_all_as().get(i).last_hart_time=now_time;
				}
			}
			
			if(svhashid.equals("*")) {
				AS temp_as=mc.as_table.get_as_by_id(node_id);
				temp_as.last_hart_time=now_time;
				ArrayList<Service> service_list=temp_as.get_all_service();
				for(int i=0;i<service_list.size();i++) {
					service_list.get(i).last_hart_time=now_time;
				}
			}else{
				AS temp_as=mc.as_table.get_as_by_id(node_id);
				temp_as.last_hart_time=now_time;
				String[] svhashid_array=svhashid.split("\\|");
				for(int i=0;i<svhashid_array.length;i++) {
					ArrayList<Service> service_list=temp_as.get_all_service_by_svhashid(svhashid_array[i]);
					for(int j=0;j<service_list.size();j++) {
						service_list.get(j).last_hart_time=now_time;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 监控任务
	 */
	private void monitor_task() {
		LinkedList<AS> hart_outtime_as_list=new LinkedList<>();
		LinkedList<Service> hart_outtime_service_list=new LinkedList<>();
		ArrayList<AS> temp_as_list=mc.as_table.get_all_as();
		for(int i=0;i<temp_as_list.size();i++) {
			AS temp_as=temp_as_list.get(i);
			long now_time=System.currentTimeMillis();
			long as_last_hart_time=temp_as.last_hart_time;
			long time_step=now_time-as_last_hart_time;
			//System.out.println(as_last_hart_time+"==="+time_step);
			//监控节点
			if(time_step>mc.config_obj.as_hart_outline_time*1000) {
				hart_outtime_as_list.add(temp_as);
				continue;
			}
			//监控service
			ArrayList<Service> temp_service_list=temp_as.get_all_service();
			for(int j=0;j<temp_service_list.size();j++) {
				if((now_time-temp_service_list.get(j).last_hart_time)>mc.config_obj.service_hart_outline_time*1000) {
					hart_outtime_service_list.add(temp_service_list.get(j));
				}
			}
		}
		System.out.println("hart_outtime_service_list====================================>"+hart_outtime_as_list.toString());
		this.outline_service(hart_outtime_service_list);
		this.outline_as(hart_outtime_as_list);
	}
	
	/**
	 * 将节点下线
	 * @param as_list
	 */
	private void outline_as(LinkedList<AS> as_list) {
		for(int i=0;i<as_list.size();i++) {
			//System.out.println(as_list.get(i).node_id);
			this.mc.service_AS_controller.as_logout(as_list.get(i));
		}
	}
	
	/**
	 * 将服务下线
	 * @param as_list
	 */
	private void outline_service(LinkedList<Service> service_list) {
		for(int i=0;i<service_list.size();i++) {
			this.mc.service_AS_controller.service_logout(service_list.get(i));
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