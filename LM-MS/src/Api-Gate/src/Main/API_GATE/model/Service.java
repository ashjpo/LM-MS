package Main.API_GATE.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * 
 * @author mmy
 *
 *	service_id							service_id
 *	api-gate-name						暴漏给的api网关
 *	服务类型								service_type(common,error,nodirect,fusing)
 *	hashid（服务名+版本号）				hashid
 *	服务名 								service_name 
 *	版本号 								version
 *	注册时间 							register_time
 *	请求列表									ask_list										
 *	as信息								as_mes
 *	服务信息								service_mes
 *		语言								
 *
 */
public class Service{
	public String service_id;
	public String call_type;
	public String api_gate_name;
	public String service_type;
	public String svhashid;
	public String service_name;
	public String service_host;
	public String service_port;
	public String service_url;
	public String version;
	public long register_time;
	public ArrayList<Ask> ask_list=new ArrayList<>();
	public AS as_mes;
	
	public boolean if_start_on_node=false;
	public boolean if_regist_on_api_gate=false;
	//心跳包时间
	public long last_hart_time;
	
	/*//熔断器
	public FusingControl uFusingControl;
	//限流器
	public TokenBucket uTokenBucket;
	//服务降级
	//[TODO]*/
	public Service(JSONObject service_mes) {
		//System.out.println(service_mes);
		
		last_hart_time=System.currentTimeMillis();
		try {
			this.service_id=service_mes.getString("service_id");
			this.service_host=service_mes.getString("service_host");
			this.service_port=service_mes.getString("service_port");
			this.call_type=service_mes.getString("call_type");
			if(this.call_type.equals("rpc")) {
				this.service_url=service_mes.getString("service_url");
			}
			this.api_gate_name=service_mes.getString("api-gate");
			this.service_type=service_mes.getString("service_type");
			this.service_name=service_mes.getString("service_name");
			//System.out.println(service_name);
			this.version=service_mes.getString("version");
			this.svhashid=this.service_name+this.version;
			register_time=System.currentTimeMillis();
			/*JSONArray ask_list_json=service_mes.getJSONArray("ask_list");
			for(int i=0;i<ask_list_json.length();i++) {
				//ask_list.add((Ask)ask_list_json.getClass());
				
			}*/
			JSONArray ask_mes=service_mes.getJSONArray("functions");
			for(int i=0;i<ask_mes.length();i++) {
				JSONObject temp_obj=ask_mes.getJSONObject(i);
				//System.out.println(call_type);
				if(call_type.equals("http")) {
					Ask_http tAsk_http=new Ask_http(temp_obj, service_host, service_port);
					tAsk_http.call_type="http";
					//System.out.println("ppp");
					ask_list.add(tAsk_http);
				}else if(call_type.equals("rpc")) {
					Ask_rpc tAsk_rpc=new Ask_rpc(temp_obj, service_host, service_port, service_url);
					tAsk_rpc.call_type="rpc";
					ask_list.add(tAsk_rpc);
				}
			}
			if_start_on_node=true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 完成在api-gate上的注册
	 */
	public void regist_service_on_api_gate() {
		if_regist_on_api_gate=true;
	}
	
	/**
	 * 关联as的节点信息
	 * @param as_mes
	 */
	public void link_to_as(AS as_mes) {
		if_start_on_node=true;
		this.as_mes=as_mes;
	}
	
	
	
}








