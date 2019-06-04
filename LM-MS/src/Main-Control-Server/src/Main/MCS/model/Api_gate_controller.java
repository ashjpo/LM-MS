package Main.MCS.model;

import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class Api_gate_controller{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public Api_gate_controller(Main_Context mc) {
		this.mc=mc;
	}
	
	/**
	 * 节点加入
	 * @param message
	 */
	public void api_gate_join(String message) {
		//{"mode":"api-gate-hard","function":"Api-Gate-node-join","api-gate-name":"x"}
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String api_gate_name=json_obj.getString("api-gate-name");
			Api_gate api_gate=new Api_gate(api_gate_name);
			this.mc.api_gate_table.add_as(api_gate);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void api_gate_logout(Api_gate api_gate) {
		this.mc.api_gate_table.add_as(api_gate);
		api_gate=null;
	}
	
	public void api_gate_logout_name(String api_gate_name) {
		Api_gate api_gate=this.mc.api_gate_table.get_api_gate_by_name(api_gate_name);
		this.mc.api_gate_table.delete_api_gate(api_gate);
	}
}