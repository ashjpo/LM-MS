package Main.AS.message;

import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class message_show{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;

	public message_show(Main_Context mc) {
		this.mc=mc;
		
	}
	
	public void get_message(String message) {
		if(mmy_Json.isJson(message)) {
			JSONObject json_obj=mmy_Json.get_jsonobj(message);
			try {
				String function = json_obj.getString("function");
				String mode = json_obj.getString("mode");
				if(!function.equals("") && !mode.equals("")) {
					if(mode.equals("AS")) {
						String node = json_obj.getString("node");
						if(function.equals("show-as-service-msg") && (node.equals(this.mc.as_controller_obj.node_id) || node.equals("*"))) {
							this.mc.as_controller_obj.send_as_service_message_show();
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}	
}