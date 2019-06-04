package Main.MCS.message.logout;


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
					
				}else if(node_type.equals("as")) {
					String node_id=json_obj.getString("node-id");
					this.mc.service_AS_controller.as_logout_id(node_id);
				}else if(node_type.equals("api-gate")) {
					String api_gate_name=json_obj.getString("api-gate-name");
					this.mc.api_gate_controller.api_gate_logout_name(api_gate_name);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}
}