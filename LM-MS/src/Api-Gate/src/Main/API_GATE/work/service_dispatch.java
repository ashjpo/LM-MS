package Main.API_GATE.work;

import java.util.Map;
import java.util.Map.Entry;

import Context.Main_Context;
import Main.API_GATE.functions.cache.cache_main;
import Main.API_GATE.functions.ddos.ddos_control;
import Main.API_GATE.functions.flow.FusingControl;
import Main.API_GATE.functions.flow.TokenBucket;
import Main.API_GATE.functions.service_reduce.service_reduce_main;
import Main.API_GATE.functions.wblist.iplist;
import Main.API_GATE.model.Ask;
import Main.API_GATE.model.Service;
import Main.API_GATE.service.HttpContext;

/**
 * ת����������
 * 
 * ���ڵ��ȷ��������
 * ����������������Ӧ��λ��
 * 
 * 
 * 
											|---------------------------------����<-|
											|										|
											|				|->cache����->����		|
											|				|						|
request->��/������->ȫ������->��������->ת����������->�ҵ�->cache�ж�->������cache->���񽵼��ж�->�����۶�->��������->SLB->����->ͬ��/�첽����
											|			  |							|			|			|
											|			  |->cache������-------------			|			|
											|													|			|
											|													|			|
											|->û���ҵ�ƥ��->Ĭ��/��/����<------------------------|-----------|
																				|
																				|
																				|--->����
 *	����service_dispatch_table,response_controller
 *			
 * @author mmy
 *
 */
public class service_dispatch{
	final boolean if_debug=false;
	Main_Context mc;
	
	iplist iplist_obj;
	ddos_control ddos_control_obj;
	TokenBucket tokenBucket_obj;
	response_controller response_controller_obj;
	cache_main cache_main_obj;
	service_reduce_main service_reduce_main_obj;
	ask_service_interface ask_service_interface;
	public service_dispatch(Main_Context mc) {
		this.mc=mc;
		
		iplist_obj=new iplist(mc);
		ddos_control_obj=new ddos_control(mc);
		tokenBucket_obj=mc.main_tb;
		response_controller_obj=new response_controller(mc);
		cache_main_obj=new cache_main(mc);
		service_reduce_main_obj=new service_reduce_main(mc);
		this.set_finish_dispatch_lisener();
		
	}
	
	/**
	 * ����http����(��ʽ����)
	 * @param httpContext
	 */
	public boolean deal_request(HttpContext httpContext) {
		String request_ip=httpContext.getRequest().get_request_ip();
		//�жϰ�����
		boolean if_can_iplist_pass=iplist_obj.if_can_pass(request_ip);
		if(!if_can_iplist_pass) {
			response_controller_obj.forbid_response(httpContext);
			return false;
		}
		
		//ddos
		boolean if_can_ddos_pass=ddos_control_obj.set_ip_if_forbid(request_ip);
		if(if_can_ddos_pass) {
			response_controller_obj.forbid_response(httpContext);
			return false;
		}
		
		//ȫ������
		String requestHeader=httpContext.getRequest().get_httpHeader();
		boolean tokens = tokenBucket_obj.getTokens(new StringBuilder(requestHeader).toString().getBytes());
		if (!tokens) {
			response_controller_obj.forbid_response(httpContext);
			return false;
		}
		//��ѯ����
		if(if_debug) {
			System.out.println("��ѯ����");
		}
		String call_url=httpContext.getRequest().getUri();
		Object match_obj=this.mc.service_dispatch_table_obj.get_match_ask(call_url);
		String call_type="single"; //aggregation
		match_ask match_ask_obj=null;
		match_ask_aggregation match_ask_aggregation_obj=null;
		if(match_obj==null) {
			response_controller_obj.no_request_response(httpContext);
			return false;
		}else {
			if(match_obj instanceof match_ask) {			//��������
				call_type="single";
				match_ask_obj=(match_ask)match_obj;
			}else if(match_obj instanceof match_ask_aggregation) {	//�������
				call_type="aggregation";
				match_ask_aggregation_obj=(match_ask_aggregation)match_obj;
				//[TODO]
				return deal_with_aggregation(httpContext,match_ask_aggregation_obj);
			}else {
				response_controller_obj.no_request_response(httpContext);
				return false;
			}
		}
		
		//cache
		String cache_type=getMapFirstAsk(match_ask_obj.ask_map).cache;
		if(if_debug) {
			System.out.println("cache_type>>>"+cache_type);
		}
		if(!cache_type.equals("")) {
			String cache_string=cache_main_obj.get_cache(getMapFirstAsk(match_ask_obj.ask_map).get_cache_type(),match_ask_obj);
			if(cache_string!=null) {
				if(!cache_string.equals("")) {
					response_controller_obj.text_response(httpContext,cache_string);
				}
			}
			
		}
		
		
		//���񽵼�
		if(if_debug) {
			System.out.println("���񽵼��ж�");
		}
		if(!service_reduce_main_obj.if_reduce_service(match_ask_obj)) {
			
		}else {
			//[TODO]
			
			return deal_with_reduce_service();
		}
		
		//��ȡ��Ӧservice_dealer
		if(if_debug) {
			System.out.println("��ȡ��Ӧservice_dealer");
		}
		service_dealer service_dealer_obj=this.mc.service_dispatch_table_obj.get_service_dealer(getMapFirstService(match_ask_obj.service_map).service_name,getMapFirstService(match_ask_obj.service_map).version);
		service_dealer_obj.set_ask_lisener(ask_service_interface);
		//�۶�
		if(if_debug) {
			System.out.println("service �۶�");
		}
		boolean if_through=service_dealer_obj.if_through();
		if(!if_through) {
			response_controller_obj.limit_flow_response(httpContext);
			return false;
		}
		
		//����
		if(if_debug) {
			System.out.println("service ����");
		}
		boolean if_hash_token=service_dealer_obj.getTokens(httpContext);
		if(!if_hash_token) {
			response_controller_obj.limit_flow_response(httpContext);
			return false;
		}
		
		//SLB
		if(if_debug) {
			System.out.println("SLB");
		}
		Service slb_service=service_dealer_obj.get_slb_service("loop");
		if(if_debug) {
			System.out.println("SLB>>>"+slb_service.service_id);
		}
		if(slb_service==null) {
			response_controller_obj.no_request_response(httpContext);
			return false;
		}
		if(if_debug) {
			System.out.println("��ѯ����");
		}
		service_dealer_obj.find_data(slb_service, match_ask_obj, httpContext,null);
		return true;
		
	}
	
	/**
	 * ����ۺ������е�ÿһ������
	 * @param httpContext
	 * @param match_ask_aggregation
	 * @return
	 */
	public boolean deal_request_aggregation(HttpContext httpContext,String call_url,aggregation_dealer aggregation_dealer_obj) {
		Object match_obj=this.mc.service_dispatch_table_obj.get_match_ask(call_url);
		String call_type="single";
		match_ask match_ask_obj=null;
		call_type="single";
		match_ask_obj=(match_ask)match_obj;
		
		//cache
		String cache_type=getMapFirstAsk(match_ask_obj.ask_map).cache;
		if(!cache_type.equals("")) {
			String cache_string=cache_main_obj.get_cache(getMapFirstAsk(match_ask_obj.ask_map).get_cache_type(),match_ask_obj);
			
			if(cache_string!=null) {
				if(!cache_string.equals("")) {
					response_controller_obj.text_response(httpContext,cache_string);
				}
			}
		}
		
		
		//���񽵼�
		if(!service_reduce_main_obj.if_reduce_service(match_ask_obj)) {
			
		}else {
			//[TODO]
			
			return deal_with_reduce_service();
		}
		
		//��ȡ��Ӧservice_dealer
		service_dealer service_dealer_obj=this.mc.service_dispatch_table_obj.get_service_dealer(getMapFirstService(match_ask_obj.service_map).service_name,getMapFirstService(match_ask_obj.service_map).version);
		service_dealer_obj.set_ask_lisener(ask_service_interface);
		//�۶�
		boolean if_through=service_dealer_obj.if_through();
		if(!if_through) {
			response_controller_obj.limit_flow_response(httpContext);
			return false;
		}
		
		//����
		boolean if_hash_token=service_dealer_obj.getTokens(httpContext);
		if(!if_hash_token) {
			response_controller_obj.limit_flow_response(httpContext);
			return false;
		}
		
		//SLB
		Service slb_service=service_dealer_obj.get_slb_service("loop");
		if(if_debug) {
			System.out.println("SLB>>>"+slb_service.service_id);
		}
		if(slb_service==null) {
			response_controller_obj.no_request_response(httpContext);
			return false;
		}
		
		service_dealer_obj.find_data(slb_service, match_ask_obj, httpContext,aggregation_dealer_obj);
		return true;
	}
	
	/**
	 * ��ɵ���ע�����
	 */
	public void set_finish_dispatch_lisener() {
		this.ask_service_interface=new ask_service_interface() {
			
			@Override
			public void request_rpc_ok(HttpContext httpContext,String return_message, String syn_asyn,match_ask match_ask_obj,aggregation_dealer aggregation_dealer_obj) {
				if(aggregation_dealer_obj!=null) {	//�ۺ�
					aggregation_dealer_obj.get_one_back_ask(match_ask_obj, return_message,true);
				}else {
					if(return_message.equals("")) {
						response_controller_obj.error_response(httpContext);
					}else {
						response_controller_obj.text_response(httpContext,return_message);
					}
					//������
					String cache_type=getMapFirstAsk(match_ask_obj.ask_map).cache;
					if(!cache_type.equals("")) {
						cache_main_obj.set_cache(return_message, "text", match_ask_obj, true);
					}
				}
				
			}
			
			@Override
			public void request_http_ok(HttpContext httpContext,String return_message,match_ask match_ask_obj,aggregation_dealer aggregation_dealer_obj) {
				if(aggregation_dealer_obj!=null) {	//�ۺ�
					aggregation_dealer_obj.get_one_back_ask(match_ask_obj, return_message,true);
				}else {
					if(return_message.equals("")) {
						response_controller_obj.error_response(httpContext);
					}else {
						response_controller_obj.html_response(httpContext,return_message);
					}
					//������
					String cache_type=getMapFirstAsk(match_ask_obj.ask_map).cache;
					if(!cache_type.equals("")) {
						cache_main_obj.set_cache(return_message, "html", match_ask_obj, true);
					}
				}
			}
			
			@Override
			public void request_error(HttpContext httpContext,match_ask match_ask_obj,aggregation_dealer aggregation_dealer_obj) {
				if(aggregation_dealer_obj!=null) {	//�ۺ�
					aggregation_dealer_obj.get_one_back_ask(match_ask_obj,"",false);
				}else {
					response_controller_obj.error_response(httpContext);
				}
			}
		};
	}
	
	/**
	 * ����ۺ��������
	 * @param httpContext
	 * @param match_ask_aggregation_obj
	 * @return
	 */
	public boolean deal_with_aggregation(HttpContext httpContext,match_ask_aggregation match_ask_aggregation_obj) {
		aggregation_dealer aggregation_dealer=new aggregation_dealer(httpContext, response_controller_obj, match_ask_aggregation_obj);
		aggregation_dealer.send_aggregation_ask(this);
		return true;
	}
	
	public boolean deal_with_reduce_service() {
		return true;
	}
	
	private Ask getMapFirstAsk(Map<String, Ask> ask_map) {    	
		Ask obj = null;        
		for (Entry<String, Ask> entry : ask_map.entrySet()) {            
			obj = entry.getValue();            
			if (obj != null) {                
				break;            
			}        
		}        
		return  obj;    
	}
	
	private Service getMapFirstService(Map<String, Service> ask_map) {    	
		Service obj = null;        
		for (Entry<String, Service> entry : ask_map.entrySet()) {            
			obj = entry.getValue();            
			if (obj != null) {                
				break;            
			}        
		}        
		return  obj;    
	}

	
	
	
	
	
	
	
	
	
	
	
}