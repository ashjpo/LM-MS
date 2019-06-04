package Main.MCS.message.mcs_control;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_control_main{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	
	mcs_control_AS mcs_control_AS_obj;
	mcs_control_api_gate mcs_control_api_gate_obj;
	mcs_control_all mcs_control_all_obj;
	public mcs_control_main(Main_Context mc) {
		this.mc=mc;
		this.mcs_control_AS_obj=new mcs_control_AS(mc);
		this.mcs_control_api_gate_obj=new mcs_control_api_gate(mc);
		this.mcs_control_all_obj=new mcs_control_all(mc);
	}
	
	public void get_message(String message) {
		if(mmy_Json.isJson(message)) {
			JSONObject json_obj=mmy_Json.get_jsonobj(message);
			try {
				String function = json_obj.getString("function");
				String mode = json_obj.getString("mode");
				if(!function.equals("") && !mode.equals("")) {
					if(mode.equals("AS")) {
						this.mcs_control_AS_obj.get_message(message);
					}else if(mode.equals("api-gate")) {
						this.mcs_control_api_gate_obj.get_message(message);
					}else if(mode.equals("MCS")) {
						//[TODO]
					}else if(mode.equals("all")) {
						this.mcs_control_all_obj.get_message(message);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}
}