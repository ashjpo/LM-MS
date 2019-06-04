package Main.API_GATE.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ask_rpc extends Ask{
	public String syn_asyn="syn";
	public String source_url;
	public ArrayList<Map<String,String>> params_list=new ArrayList<>();
	public Ask_rpc(JSONObject function,String service_host,String service_port,String service_url) throws JSONException {
		super(function);
		source_url="tcp://"+service_host+":"+service_port+"/"+service_url;
		syn_asyn=function.getString("syn_asyn");
		JSONArray param=function.getJSONArray("params");
		for(int i=0;i<param.length();i++) {
			Map<String,String> temp_map=new HashMap<>();
			temp_map.put("http_param", param.getJSONArray(i).getString(0));
			temp_map.put("rpc_param", param.getJSONArray(i).getString(1));
			temp_map.put("param_type", param.getJSONArray(i).getString(2));
			params_list.add(temp_map);
		}
	}
	
	
	
}