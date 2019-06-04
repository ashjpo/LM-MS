package Main.AS.message;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class as_control_main{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	
	as_control_AS mcs_control_AS_obj;
	as_control_AS_hard mcs_control_AS_hard_obj;
	public as_control_main(Main_Context mc) {
		this.mc=mc;
		this.mcs_control_AS_obj=new as_control_AS(mc);
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
					}else if(mode.equals("AS-hard")) {
						this.mcs_control_AS_hard_obj.get_message(message);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}
}