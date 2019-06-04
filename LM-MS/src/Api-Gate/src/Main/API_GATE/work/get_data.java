package Main.API_GATE.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Main.API_GATE.model.Ask;
import Main.API_GATE.model.Ask_http;
import Main.API_GATE.model.Ask_rpc;
import Main.API_GATE.model.Service;
import Main.API_GATE.service.HttpContext;
import Tools.HttpRequestUtil;
import hprose.client.HproseTcpClient;
import hprose.common.InvokeSettings;
import hprose.util.concurrent.Promise;

public class get_data{
	//超时时间
	int out_time=60;
	
	/**
	 * 包装了异步和同步请求
	 * @param service
	 * @param ask
	 * @param httpContext
	 * @return
	 */
	public String  get_rpc(Service service,Ask_rpc ask,HttpContext httpContext) {
		if(ask.syn_asyn.equals("syn")) {
			return this.get_rpc_syn(service, ask, httpContext);
		}else {
			this.get_rpc_asyn(service, ask, httpContext);
			JSONObject jsonObject=new JSONObject();
			try {
				jsonObject.put("return_code",0);
				jsonObject.put("mes","");
				return jsonObject.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("error rpc");
				return "";
			}
		}
	}
	
	/**
	 * 同步获取rpc数据
	 * @return
	 */
	public String get_rpc_syn(Service service,Ask_rpc ask,HttpContext httpContext) {
		HproseTcpClient client = new HproseTcpClient();
		InvokeSettings settings = new InvokeSettings();
		settings.setTimeout(out_time*1000);
		
		String host=service.service_host;
		String port=service.service_port;
		String service_url=service.service_url;
		//System.out.println(">>>tcp://"+host+":"+port+"/"+service.service_id+service_url);
        client.useService("tcp://"+host+":"+port+"/"+service.service_id+service_url);
        Map<String,String> map_param_http=merage_map(httpContext.getRequest().get_getparams(),httpContext.getRequest().get_postparams());
        //System.out.println(map_param_http.toString());
        //System.out.println("http://"+host+":"+port+"/"+service.service_id+service_url);
        
        
        
        JSONArray param_json=ask.params;
        ArrayList<Object> temp_object_list=new ArrayList<>();
        for(int i=0;i<param_json.length();i++) {
        	String http_param;
			try {
				http_param = param_json.getJSONArray(i).getString(0);
				String rpc_param=param_json.getJSONArray(i).getString(1);
	        	String type=param_json.getJSONArray(i).getString(2);
	        	temp_object_list.add(map_param_http.get(http_param));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        //System.out.println(temp_object_list.toString());
        
        Object[] obj_params=temp_object_list.toArray();
		try {
			long t1=System.currentTimeMillis();
			String result = client.invoke(ask.ask_name, obj_params, settings).toString();
			long t2=System.currentTimeMillis();
			//System.out.println("======"+(t2-t1));
			return result;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error rpc");
			return "";
		}
	}
	
	/**
	 * 异步获取rpc数据
	 * @return
	 */
	public String get_rpc_asyn(Service service,Ask_rpc ask,HttpContext httpContext) {
		HproseTcpClient client = new HproseTcpClient();
		InvokeSettings settings = new InvokeSettings();
		settings.setTimeout(out_time*1000);
		settings.setAsync(true);
		String host=service.service_host;
		String port=service.service_port;
		String service_url=service.service_url;
        client.useService("tcp://"+host+":"+port+"/"+service.service_id+service_url);
        Map<String,String> map_param_http=merage_map(httpContext.getRequest().get_getparams(),httpContext.getRequest().get_postparams());
        JSONArray param_json=ask.params;
        ArrayList<Object> temp_object_list=new ArrayList<>();
        for(int i=0;i<param_json.length();i++) {
        	String http_param;
			try {
				http_param = param_json.getJSONArray(i).getString(0);
				String rpc_param=param_json.getJSONArray(i).getString(1);
	        	String type=param_json.getJSONArray(i).getString(2);
	        	temp_object_list.add(map_param_http.get(http_param));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        Object[] obj_params=temp_object_list.toArray();
		try {
			long t1=System.currentTimeMillis();
			String result = client.invoke(ask.ask_name, obj_params, settings).toString();
			long t2=System.currentTimeMillis();
			//System.out.println("======"+(t2-t1));
			return result;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("error rpc");
			return "";
		}
	}
	
	/**
	 * 同步获取http数据
	 * @return
	 */
	public String get_http_syn(Service service,Ask_http ask,HttpContext httpContext) {
		String para_get="";
		String para_post="";
		Map<String,String> temp_map_get=httpContext.getRequest().get_getparams();
		Map<String,String> temp_map_post=httpContext.getRequest().get_postparams();
		int index=0;
		for (Map.Entry<String, String> entry : temp_map_get.entrySet()) { 
			if(index==0) {
				para_get=entry.getKey()+"="+entry.getValue();
			}else {
				para_get=para_get+"&"+entry.getKey()+"="+entry.getValue();
			}
			index++;
		}
		
		for (Map.Entry<String, String> entry : temp_map_post.entrySet()) { 
			if(index==0) {
				para_post=entry.getKey()+"="+entry.getValue();
			}else {
				para_post=para_post+"&"+entry.getKey()+"="+entry.getValue();
			}
			index++;
		}
		String sr="";
		
		if(ask.post_or_get.equals("get")) {
			sr=HttpRequestUtil.sendGet(ask.source_url, para_get);
		}else {
			String url="http://"+ask.source_url+"?"+para_get;
			sr=HttpRequestUtil.sendPost(url, para_post, false);
		}
		return sr;
	}
	
	/**
	 * 合并两个map
	 * 去除重复的数据
	 * @param m1
	 * @param m2
	 * @return
	 */
	public Map<String,String> merage_map(Map<String, String> m1,Map<String, String> m2) {
		Map<String, String> return_map=new HashMap<>();
		for (Map.Entry<String, String> entry : m1.entrySet()) { 
			if(!return_map.containsKey(entry.getKey())) {
				return_map.put(entry.getKey(), entry.getValue());
			}
		}
		for (Map.Entry<String, String> entry : m2.entrySet()) { 
			if(!return_map.containsKey(entry.getKey())) {
				return_map.put(entry.getKey(), entry.getValue());
			}
		}
		return return_map;
	}
}