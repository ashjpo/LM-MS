package Main.API_GATE.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Context.Main_Context;
import Main.API_GATE.functions.flow.FusingControl;
import Main.API_GATE.functions.flow.TokenBucket;
import Main.API_GATE.functions.slb.service_load_balance;
import Main.API_GATE.functions.slb.service_request_record;
import Main.API_GATE.model.Ask;
import Main.API_GATE.model.Ask_http;
import Main.API_GATE.model.Ask_rpc;
import Main.API_GATE.model.Service;
import Main.API_GATE.service.HttpContext;

public class service_dealer{
	public ArrayList<String> serviceid_list=new ArrayList<>();
	public ArrayList<Service> service_list=new ArrayList<>();
	public String service_name;
	public String version;
	ExecutorService fixedThreadPool;
	Main_Context mc;
	TokenBucket tokenBucket;
	FusingControl fusingControl;
	//请求记录
	service_request_record service_request_record=new service_request_record();
	//SLB
	service_load_balance service_load_balance=new service_load_balance();
	ArrayList<ask_service_interface> ask_service_interfaces_list=new ArrayList<>();
	/**
	 * 更新请求记录表 这个表主要用于slb
	 */
	public void update_service_request_recorder() {
		Map<String,Service> temp_service_map=new HashMap<>();
		Map<String,Integer> temp_call_num_map=new HashMap<>();
		//System.out.println("update>>>"+serviceid_list.size());
		for(int i=0;i<serviceid_list.size();i++) {
			String serviceid=serviceid_list.get(i);
			if(service_request_record.service_map.containsKey(serviceid)) {
				temp_service_map.put(serviceid, service_request_record.service_map.get(serviceid));
				temp_call_num_map.put(serviceid, service_request_record.call_num_map.get(serviceid));
			}else {
				Service temp_service=service_list.get(i);
				temp_service_map.put(serviceid, temp_service);
				temp_call_num_map.put(serviceid,0);
			}
		}
		service_request_record.service_map=temp_service_map;
		service_request_record.call_num_map=temp_call_num_map;
		
	}
	
	public service_dealer(Main_Context mc) {
		this.mc=mc;
		//模拟
		int pooling_num=10;
		fixedThreadPool = Executors.newFixedThreadPool(pooling_num);
		tokenBucket=TokenBucket.newBuilder().avgFlowRate(mc.config_obj.service_default_average_flow).maxFlowRate(mc.config_obj.service_default_max_flow).backet_size(mc.config_obj.service_default_bucket_size).build();
		fusingControl=FusingControl.newFusingControl(mc).set_limit_fusing_error_times(mc.config_obj.limit_fusing_error_times).set_recover_open_times(mc.config_obj.recover_open_times).set_recover_midopen_time(mc.config_obj.recover_midopen_time);
		//设置参数
	}
	
	public service_dealer(Main_Context mc,int pooling_num) {
		this.mc=mc;
		fixedThreadPool = Executors.newFixedThreadPool(pooling_num);
		//模拟
		tokenBucket=TokenBucket.newBuilder().avgFlowRate(51200).maxFlowRate(102400).build();
		fusingControl=new FusingControl(mc);
		//设置参数
	}
	
	public boolean getTokens(HttpContext httpContext) {
		boolean tokens = tokenBucket.getTokens(new StringBuilder(httpContext.getRequest().get_httpHeader()).toString().getBytes());
		return tokens;
	}
	
	public boolean if_through() {
		return fusingControl.if_through();
	}
	
	public void set_ask_ok() {
		fusingControl.set_ask_ok();
	}
	
	public void set_ask_no() {
		fusingControl.set_ask_no();
	}
	
	/**
	 * 刷新service_request_record的call_num_map
	 */
	public void flush_service_request_record() {
		for (Map.Entry<String, Integer> entry : service_request_record.call_num_map.entrySet()) { 
			String key=entry.getKey();
			service_request_record.call_num_map.put(key, 0);
		}
	}
	
	/**
	 * 获取经过slb后分配的service
	 * @param slb_type
	 * @return
	 */
	public Service get_slb_service(String slb_type) {
		Service service=null;
		if(slb_type.equals("loop")) {
			service=service_load_balance.slb_get_service_loop(service_request_record);
		}else if(slb_type.equals("random")) {
			service=service_load_balance.slb_get_service_random(service_request_record);
		}else if(slb_type.equals("weight_loop")) {
			service=service_load_balance.slb_get_service_weight_loop(service_request_record);
		}else {
			service=service_load_balance.slb_get_service_loop(service_request_record);
		}
		
		return service;
	}
	
	/**
	 * 向相应服务发送请求
	 * @param service
	 * @param match_ask
	 */
	public void find_data(Service service_obj,match_ask match_ask_obj,HttpContext httpContext,aggregation_dealer aggregation_dealer_obj) {
		String service_id=service_obj.service_id;
		//System.out.println("find data");
		if(match_ask_obj.ask_map.containsKey(service_id)) {
			if(match_ask_obj.ask_map.get(service_id).call_type.equals("rpc")) {
				//System.out.println("find data rpc");
				Ask_rpc ask_rpc=(Ask_rpc)match_ask_obj.ask_map.get(service_id);
				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						String return_mes=mc.get_data_obj.get_rpc(service_obj, ask_rpc, httpContext);
						for(int i=0;i<ask_service_interfaces_list.size();i++) {
							ask_service_interfaces_list.get(i).request_rpc_ok(httpContext,return_mes,ask_rpc.syn_asyn,match_ask_obj,aggregation_dealer_obj);
						}
					}
				});
			}else if(match_ask_obj.ask_map.get(service_id).call_type.equals("http")) {
				//System.out.println("find data http");
				Ask_http ask_http=(Ask_http)match_ask_obj.ask_map.get(service_id);
				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						String return_mes=mc.get_data_obj.get_http_syn(service_obj, ask_http, httpContext);
						for(int i=0;i<ask_service_interfaces_list.size();i++) {
							ask_service_interfaces_list.get(i).request_http_ok(httpContext,return_mes,match_ask_obj,aggregation_dealer_obj);
						}
					}
				});
			}
		}else {
			fixedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					//模拟请求
					for(int i=0;i<ask_service_interfaces_list.size();i++) {
						ask_service_interfaces_list.get(i).request_error(httpContext,match_ask_obj,aggregation_dealer_obj);
					}
				}
			});
		}
	}
	
	/**
	 * 设置请求返回监听
	 */
	public void set_ask_lisener(ask_service_interface ask_service_interface) {
		if(!ask_service_interfaces_list.contains(ask_service_interface)) {
			ask_service_interfaces_list.add(ask_service_interface);
		}
	}
	
	
}