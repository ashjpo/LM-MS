package Main.API_GATE.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author mmy
 */
public class Ask{
	//ask-name
	public String ask_name;
	public String call_type="rpc";
	public String cache="";
	public JSONArray params=new JSONArray();
	
	public String http_url;
	
	public Ask(JSONObject function) {
		
		try {
			cache=function.getString("cache");
		} catch (Exception e) {
			// TODO: handle exception
			cache="";
		}
		
		try {
			ask_name=function.getString("name");
			http_url=function.getString("http_url");
			params=function.getJSONArray("params");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String get_cache_type() {
		return this.cache;
	}
	
	
	
}