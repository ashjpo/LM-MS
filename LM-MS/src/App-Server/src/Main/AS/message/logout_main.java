package Main.AS.message;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class logout_main{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public logout_main(Main_Context mc) {
		this.mc=mc;

	}
	
	public void get_message(String message) {
		if(mmy_Json.isJson(message)) {
			JSONObject json_obj=mmy_Json.get_jsonobj(message);
			try {
				String node_type = json_obj.getString("node-type");
				if(node_type.equals("mcs")) {
					//是否结束节点
					
				}else if(node_type.equals("as")) {
					
				}else if(node_type.equals("api-gate")) {
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}
}