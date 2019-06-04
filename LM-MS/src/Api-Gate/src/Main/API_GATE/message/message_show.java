package Main.API_GATE.message;


import org.json.JSONArray;
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
					if(mode.equals("Api-Gate")) {
						if(function.equals("show-as-service-msg")) {
							String gate_name=json_obj.getString("api-gate-name");
							if(gate_name.equals(mc.gate_name) || gate_name.equals("*")) {
								JSONObject sendjson=new JSONObject();
								JSONArray sendjson_service=new JSONArray();
								JSONArray sendjson_as=new JSONArray();
								for(int i=0;i<mc.service_table.get_all_service().size();i++) {
									JSONObject sjsonObject=new JSONObject();
									sjsonObject.put("service", mc.service_table.get_all_service().get(i).service_name);
									sjsonObject.put("version", mc.service_table.get_all_service().get(i).version);
									sjsonObject.put("service_id", mc.service_table.get_all_service().get(i).service_id);
									sendjson_service.put(sjsonObject);
									
								}
								for(int i=0;i<mc.as_table.get_all_as().size();i++) {
									JSONObject sjsonObject=new JSONObject();
									sjsonObject.put("node",mc.as_table.get_all_as().get(i).node_id);
									sendjson_as.put(sjsonObject);
								}
								sendjson.put("service-mes", sendjson_service);
								sendjson.put("as-mes", sendjson_as);
								mc.mqtt_obj.send_message(sendjson.toString(),mc.config_obj.message_show_back);		
							}
							
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}	
}