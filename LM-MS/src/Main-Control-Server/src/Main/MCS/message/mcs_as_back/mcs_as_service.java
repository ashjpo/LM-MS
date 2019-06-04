package Main.MCS.message.mcs_as_back;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_as_service{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public mcs_as_service(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("AS-start-service-back")) {	//启动服务完成
					//{"mode":"AS-service","function":"AS-start-service-back","node":"x","service-mes":[]}
					this.mc.service_AS_controller.service_register(message);
				}else if(function.equals("AS-stop-service-back")) {	//停止服务完成
					//{"mode":"AS-service","function":"AS-stop-service-back","node":"x","svhashid":"","service","version"}
					//String svhashid=json_obj.getString("svhashid");
					String service=json_obj.getString("service");
					String version=json_obj.getString("version");
					String node_id=json_obj.getString("node");
					this.mc.service_AS_controller.service_logout_name_version(service,version,node_id);
				}else if(function.equals("AS-stop-service-id-back")) {	//停止服务完成
					//{"mode":"AS-service","function":"AS-stop-service-id-back","service_id":"x"}
					String service_id=json_obj.getString("service_id");
					this.mc.service_AS_controller.service_logout_id(service_id);
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}