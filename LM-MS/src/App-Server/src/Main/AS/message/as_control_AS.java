package Main.AS.message;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class as_control_AS{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public as_control_AS(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("AS-show-service")) {	//�鿴���з���
					//{"mode":"AS","function":"AS-show-service","node":"*"}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						
					}
				}else if(function.equals("AS-start-service")) {	//����ĳ���ڵ��ĳ������ĳ��svhashid��
					//{"mode":"AS","function":"AS-start-service","service":"x","version":"*","node":"*","num":1}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						String service_name=json_obj.getString("service");
						String version=json_obj.getString("version");
						this.mc.service_controller_obj.start_service(service_name, version);
					}
					
				}else if(function.equals("AS-stop-service")) {	//ֹͣĳ���ڵ�ķ���
					//{"mode":"AS","function":"AS-stop-service","service":"x","version":"*","node":"*","num":-1}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						String service_name=json_obj.getString("service");
						String version=json_obj.getString("version");
						this.mc.service_controller_obj.stop_service(service_name, version);
					}
					
				}else if(function.equals("AS-stop-service-id")) {	//ֹͣĳ���ڵ�id�ķ���
					//{"mode":"AS","function":"AS-stop-service-id","service_id":"x1","node":"x"}}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						String service_id=json_obj.getString("service_id");
						this.mc.service_controller_obj.stop_service_by_id(service_id);
					}
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}