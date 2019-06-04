package Main.API_GATE.message;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;



/**
 * ´ý¶¨
 * @author mmy
 *
 */
public class api_gate_hard{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public api_gate_hard(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("AS-node-join")) {
					String gate_name=json_obj.getString("api-gate-name");
					if(gate_name.equals(mc.gate_name) || gate_name.equals("*")) {
						this.mc.service_AS_controller.as_join(message);
					}
					
				}else if(function.equals("AS-node-logout")) {
					String gate_name=json_obj.getString("api-gate-name");
					if(gate_name.equals(mc.gate_name) || gate_name.equals("*")) {
						String node_id=json_obj.getString("node");
						this.mc.service_AS_controller.as_logout_id(node_id);
					}
					
				}else if(function.equals("Api-Gate-close-api-gate")) {
					String gate_name=json_obj.getString("api-gate-name");
					if(gate_name.equals(mc.gate_name) || gate_name.equals("*")) {
						
					}
					
				}else if(function.equals("Api-Gate-open-api-gate")) {
					String gate_name=json_obj.getString("api-gate-name");
					if(gate_name.equals(mc.gate_name) || gate_name.equals("*")) {
						
					}
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}