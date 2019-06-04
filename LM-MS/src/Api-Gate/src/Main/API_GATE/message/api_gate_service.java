package Main.API_GATE.message;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;



/**
 * ´ý¶¨
 * @author mmy
 *
 */
public class api_gate_service{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public api_gate_service(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("AS-start-service")) {
					String gate_name=json_obj.getString("api-gate-name");
					if(gate_name.equals(mc.gate_name) || gate_name.equals("*")) {
						this.mc.service_AS_controller.service_register(message);
					}
					
				}else if(function.equals("AS-stop-service-id")) {
					String gate_name=json_obj.getString("api-gate-name");
					if(mc.gate_name.equals(gate_name) || gate_name.equals("*")) {
						String service_id=json_obj.getString("service_id");
						this.mc.service_AS_controller.service_logout_id(service_id);
					}
					
				}else if(function.equals("AS-stop-service")) {
					String gate_name=json_obj.getString("api-gate-name");
					if(mc.gate_name.equals(gate_name) || gate_name.equals("*")) {
						String service=json_obj.getString("service");
						String version=json_obj.getString("version");
						String node_id=json_obj.getString("node");
						this.mc.service_AS_controller.service_logout_name_version(service,version,node_id);
					}
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}