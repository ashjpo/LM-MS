package Main.API_GATE.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ask_http extends Ask{
	public String source_url;
	public String post_or_get;
	public Ask_http(JSONObject function,String service_host,String service_port) {
		super(function);
		try {
			post_or_get=function.getString("post_or_get");
			if(post_or_get.equals("get")) {
				post_or_get="get";
			}else {
				post_or_get="post";
			}
			source_url="http://"+service_host+":"+service_port+function.getString("source_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}