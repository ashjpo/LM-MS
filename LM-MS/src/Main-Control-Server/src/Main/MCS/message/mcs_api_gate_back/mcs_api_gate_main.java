package Main.MCS.message.mcs_api_gate_back;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_api_gate_main{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	mcs_api_gate_service mcs_api_gate_service_obj;
	mcs_api_gate_hard mcs_api_gate_hard_obj;
	public mcs_api_gate_main(Main_Context mc) {
		this.mc=mc;
		this.mcs_api_gate_service_obj=new mcs_api_gate_service(mc);
		this.mcs_api_gate_hard_obj=new mcs_api_gate_hard(mc);
	}
	
	public void get_message(String message) {
		if(mmy_Json.isJson(message)) {
			JSONObject json_obj=mmy_Json.get_jsonobj(message);
			try {
				String function = json_obj.getString("function");
				String mode = json_obj.getString("mode");
				if(!function.equals("") && !mode.equals("")) {
					if(mode.equals("api-gate-service")) {
						this.mcs_api_gate_service_obj.get_message(message);
					}else if(mode.equals("api-gate-hard")) {
						this.mcs_api_gate_hard_obj.get_message(message);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}
}