package Main.API_GATE.work;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import javax.crypto.AEADBadTagException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Main.API_GATE.model.Ask;
import Main.API_GATE.model.Service;
import Main.API_GATE.service.HttpContext;

/**
 * 聚合调用类
 * 串行/并行
 * @author mmy
 *
 */
public class aggregation_dealer{
	match_ask_aggregation match_ask_aggregation_obj;
	//用于记录完成的请求
	public ArrayList<match_ask> finish_ask=new ArrayList<>();
	public ArrayList<String> finish_message_back=new ArrayList<>();
	//超时时间
	long request_out_time=60;	//s
	Thread out_time_thread;
	Timer timer=new Timer();
	response_controller response_controller_obj;
	HttpContext httpContext;
	public aggregation_dealer(HttpContext httpContext,response_controller response_controller_obj,match_ask_aggregation match_ask_aggregation_obj) {
		this.match_ask_aggregation_obj=match_ask_aggregation_obj;
		this.response_controller_obj=response_controller_obj;
		this.httpContext=httpContext;
		//计时
		out_off_time();
	}
	
	//发送请求
	public void send_aggregation_ask(service_dispatch service_dispatch) {
		//目前只支持并行
		//[TODO]串行
		try {
			for(int i=0;i<match_ask_aggregation_obj.match_ask_list.size();i++) {
				service_dispatch.deal_request_aggregation(httpContext, match_ask_aggregation_obj.match_ask_list.get(i), this);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("aggregation url error");
			response_controller_obj.no_request_response(httpContext);
		}
		
		
	}
	
	/**
	 * 超时
	 */
	public void out_off_time() {
		out_time_thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						response_controller_obj.out_time_response(httpContext);
					}
				}, request_out_time*1000);
				
			}
		});
		out_time_thread.start();
	}
	
	/**
	 * 得到一个返回的ask
	 */
	public void get_one_back_ask(match_ask match_ask_back,String message_back,boolean if_ok) {
		synchronized (this) {
			if(!finish_ask.contains(match_ask_back)) {
				finish_ask.add(match_ask_back);
				if(!if_ok) {
					finish_message_back.add("error");
				}else {
					finish_message_back.add(message_back);
				}
			}
			//判断是否完成
			boolean if_finish=(finish_ask.size()==match_ask_aggregation_obj.match_ask_list.size());
			if(if_finish) {
				JSONObject jsonObject_all=new JSONObject();
				for(int i=0;i<finish_ask.size();i++) {
					JSONObject jsonObject=new JSONObject();
					match_ask temp_ask_back=finish_ask.get(i);
					String service_name=getMapFirstService(temp_ask_back.service_map).service_name;
					String version=getMapFirstService(temp_ask_back.service_map).version;
					String ask_name=getMapFirstAsk(temp_ask_back.ask_map).ask_name;
					String ask_url=getMapFirstAsk(temp_ask_back.ask_map).http_url;
					try {
						jsonObject.put("service_name", service_name);
						jsonObject.put("version", version);
						jsonObject.put("ask_name", ask_name);
						jsonObject.put("ask_url", ask_url);
						jsonObject.put("mes", message_back);
						jsonObject_all.put(match_ask_aggregation_obj.match_key_list.get(match_ask_aggregation_obj.match_ask_list.indexOf(temp_ask_back.url)), jsonObject);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				response_controller_obj.json_text_response(httpContext, jsonObject_all.toString());
			}else {
				//System.out.println(match_ask_back.url+"="+"C");
			}
		}
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
	
}