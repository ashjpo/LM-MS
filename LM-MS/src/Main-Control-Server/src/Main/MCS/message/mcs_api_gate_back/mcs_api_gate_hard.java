package Main.MCS.message.mcs_api_gate_back;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class mcs_api_gate_hard{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public mcs_api_gate_hard(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("Api-Gate-node-join")) {	//物理节点加入
					//{"mode":"api-gate-hard","function":"Api-Gate-node-join","api-gate-name":"x"}
					this.mc.api_gate_controller.api_gate_join(message);
				}else if(function.equals("Api-Gate-node-logout")) {	//物理节点退出
					//{"mode":"api-gate-hard","function":"Api-Gate-node-logout","api-gate-name":"x"}
					String api_gate_name=json_obj.getString("api-gate-name");
					this.mc.api_gate_controller.api_gate_logout_name(api_gate_name);
				}else if(function.equals("Api-Gate-node-hart")) {	//心跳数据
					//{"mode":"api-gate-hard","function":"Api-Gate-node-hart","api-gate-name":"x"}
					this.mc.mag_obj.get_api_gate_hart(message);
				}else if(function.equals("Api-Gate-status")) {	//状态返回
					//{"mode":"api-gate-hard","function":"Api-Gate-node-status","api-gate-name":"x","status":""}
					this.mc.mag_obj.get_api_gate_status(message);
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}