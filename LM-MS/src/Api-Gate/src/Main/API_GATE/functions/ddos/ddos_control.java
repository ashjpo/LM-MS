package Main.API_GATE.functions.ddos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Context.Main_Context;
import Tools.MMY_Config;

public class ddos_control{
	private long flush_time=30;	//ˢ��ʱ��
	private int max_request_times=60;	//ˢ��ʱ���������ʵĴ�������������ͱ��ж�Ϊddos
	private Map<String,Integer> ip_map=new HashMap<>();
	private boolean if_use=false;
	Main_Context mc;
	Thread ddos_monitor_thread;
	Timer flush_timer=new Timer();
	public ddos_control(Main_Context mc) {
		this.mc=mc;
		flush_time=mc.config_obj.flush_time;
		max_request_times=mc.config_obj.max_request_times;
		if_use=mc.config_obj.ddos_if_use;
		
		if(if_use) {
			start_monitor_ddos();
		}
	}
	
	/**
	 * ����ipͬʱ��ȡ�Ƿ�����ddos
	 * @param ip
	 * @return ����->true ������->false
	 */
	public boolean set_ip_if_forbid(String ip) {
		if(!if_use) {
			return false;
		}else {
			synchronized(this){
				if(ip_map.containsKey(ip)) {
					ip_map.put(ip,ip_map.get(ip)+1);
					if(ip_map.get(ip)>max_request_times) {
						return true;
					}else {
						return false;
					}
				}else {
					ip_map.put(ip,1);
					return false;
				}
			}
		}
		
	}
	
	/**
	 * �����Ƿ�ʹ��ddos
	 * @param if_use
	 */
	public void set_if_use(boolean if_use) {
		
		if(this.if_use==false && if_use==true) {
			start_monitor_ddos();
		}
		
		if(this.if_use==true && if_use==false) {
			stop_monitor_ddos();
		}
		
		this.if_use=if_use;
	}
	
	/**
	 * ��ʼ���ddos
	 */
	private void start_monitor_ddos() {
		ddos_monitor_thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				flush_timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						synchronized(this){
							System.out.println("ddos ip map clear");
							ip_map.clear();
						}
					}
				}, 30*1000,flush_time*1000);
			}
		});
		ddos_monitor_thread.start();
	}
	
	/**
	 * ֹͣ���ddos
	 */
	private void stop_monitor_ddos() {
		if(ddos_monitor_thread!=null) {
			ddos_monitor_thread.stop();
		}
	}
}