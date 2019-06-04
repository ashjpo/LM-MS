package Main.API_GATE.message;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class api_gate_control_main{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	
	api_gate_service api_gate_service_obj;
	api_gate_hard api_gate_hard_obj;
	public api_gate_control_main(Main_Context mc) {
		this.mc=mc;
		this.api_gate_service_obj=new api_gate_service(mc);
		this.api_gate_hard_obj=new api_gate_hard(mc);
	}
	
	public void get_message(String message) {
		if(mmy_Json.isJson(message)) {
			JSONObject json_obj=mmy_Json.get_jsonobj(message);
			try {
				String function = json_obj.getString("function");
				String mode = json_obj.getString("mode");
				if(!function.equals("") && !mode.equals("")) {
					if(mode.equals("api-gate-service")) {
						this.api_gate_service_obj.get_message(message);
					}else if(mode.equals("api-gate-hard")) {
						this.api_gate_hard_obj.get_message(message);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}
}