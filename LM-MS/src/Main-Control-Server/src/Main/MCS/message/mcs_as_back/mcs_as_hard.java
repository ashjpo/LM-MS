package Main.MCS.message.mcs_as_back;



import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_as_hard{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public mcs_as_hard(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("AS-node-join")) {	//物理节点加入
					//{"mode":"AS-hard","function":"AS-node-join","node":"x"}
					this.mc.service_AS_controller.as_join(message);
				}else if(function.equals("AS-node-logout")) {	//物理节点退出
					//{"mode":"AS-hard","function":"AS-node-logout","node":"x"}
					String node_id=json_obj.getString("node");
					this.mc.service_AS_controller.as_logout_id(node_id);
				}else if(function.equals("AS-node-hart")) {	//心跳数据
					//{"mode":"AS-hard","function":"AS-node-hart","node":"x","svhashid":""}
					this.mc.ma_obj.get_as_hart(message);
				}else if(function.equals("AS-node-status")) {	//状态返回
					//{"mode":"AS-hard","function":"AS-node-status","node":"x","svhashid":"","status":""}
					this.mc.ma_obj.get_as_status(message);
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}