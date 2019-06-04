package Main.MCS.message.mcs_control;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;



/**
 * 待定
 * @author mmy
 *
 */
public class mcs_control_all{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public mcs_control_all(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("Pause-all")) {	//暂停整套系统
					//【mode:all,function:Pause-all】
					
				}else if(function.equals("Start-all")) {	//开始整套系统
					//【mode:all,function:Start-all】
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}