package Main.AS.message;


import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

public class as_control_AS_hard{
	private MMY_Json mmy_Json=new MMY_Json();
	Main_Context mc;
	public as_control_AS_hard(Main_Context mc) {
		this.mc=mc;
	}
	
	public void get_message(String message) {
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String function = json_obj.getString("function");
			String mode = json_obj.getString("mode");
			if(!function.equals("")) {
				if(function.equals("AS-shutdown-node")) {	//�ڵ�ػ�
					//{"mode":"AS-hard","function":"AS-shutdown-node","node":"*"}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						this.mc.as_controller_obj.stop_as();
					}
				}else if(function.equals("AS-reboot-node")) {	//�ڵ�����
					//{"mode":"AS-hard","function":"AS-reboot-node","node":"*"}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						this.mc.as_controller_obj.reboot_as();
					}
				}else if(function.equals("AS-stop-node")) {	//�ڵ�رճ���
					//{"mode":"AS-hard","function":"AS-reboot-node","node":"*"}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						this.mc.as_controller_obj.stop_program_as();
					}
				}else if(function.equals("AS-restart-node")) {	//�ڵ���������
					//{"mode":"AS-hard","function":"AS-reboot-node","node":"*"}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						this.mc.as_controller_obj.restart_program_as();
					}
						
				}else if(function.equals("AS-node-status")) {	//�ڵ�״̬��ѯ
					//{"mode":"AS-hard","function":"AS-node-status",,"node":"*"}
					String node_id=json_obj.getString("node");
					if(node_id.equals(this.mc.as_controller_obj.node_id) || node_id.equals("*")) {
						this.mc.as_controller_obj.send_status_message();
					}
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}