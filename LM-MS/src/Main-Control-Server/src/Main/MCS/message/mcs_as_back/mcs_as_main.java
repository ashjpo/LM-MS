package Main.MCS.message.mcs_as_back;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_as_main{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	
	mcs_as_service mcs_as_service_obj;
	mcs_as_hard mcs_as_hard_obj;
	public mcs_as_main(Main_Context mc) {
		this.mc=mc;
		this.mcs_as_service_obj=new mcs_as_service(mc);
		this.mcs_as_hard_obj=new mcs_as_hard(mc);
	}
	
	public void get_message(String message) {
		if(mmy_Json.isJson(message)) {
			JSONObject json_obj=mmy_Json.get_jsonobj(message);
			try {
				String function = json_obj.getString("function");
				String mode = json_obj.getString("mode");
				if(!function.equals("") && !mode.equals("")) {
					if(mode.equals("AS-service")) {
						this.mcs_as_service_obj.get_message(message);
					}else if(mode.equals("AS-hard")) {
						this.mcs_as_hard_obj.get_message(message);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}
}