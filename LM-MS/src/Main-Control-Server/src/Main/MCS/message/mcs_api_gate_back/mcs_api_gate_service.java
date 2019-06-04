package Main.MCS.message.mcs_api_gate_back;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_api_gate_service{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public mcs_api_gate_service(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("Api-Gate-regist-service-back")) {	//
					//{"mode":"api-gate-service","function":"Api-Gate-regist-service-back","service_id":"x"}
					
				}else if(function.equals("Api-Gate-logout-service-back")) {	//
					//{"mode":"api-gate-service","function":"Api-Gate-logout-service-back","service_id":"x"}
					
				}else if(function.equals("Api-Gate-show-st")) {	//
					//{"mode":"api-gate-service","function":"Api-Gate-show-st","mes":""}
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}