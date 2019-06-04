package Context;

import Tools.MMY_Config;
public class Config_Context{
	private String config_root_dir=null;
	//mqtt基本配置
	public String mqtt_host=null;
	public String mqtt_port=null;
	public String mqtt_userName=null;
	public String mqtt_passWord=null;
	public String mqtt_disconnect_topic=null;
	public String mqtt_disconnect_message=null;
	public int mqtt_reconnect_times;
	
	public String mqtt_sub_message_show_back=null;
	//发送topic
	public String mqtt_mcs_control=null;
	public String mqtt_as_control=null;
	public String mqtt_api_gate_control=null;
	
	public String mqtt_mcs_message_show=null;
	public String mqtt_as_message_show=null;
	public String mqtt_api_gate_message_show=null;

	public Config_Context(String config_root_dir) {
		this.config_root_dir=config_root_dir;
		//初始化mqtt配置
		this.init_mqtt_config();		
	}
	
	
	/**
	 * 初始化mqtt配置
	 */
	private void init_mqtt_config() {
		String mqtt_config_path=this.config_root_dir+"/mqtt.properties";
		MMY_Config mqtt_config=new MMY_Config(mqtt_config_path);
		
		this.mqtt_host=mqtt_config.get_string("host", "127.0.0.1");
		this.mqtt_userName=mqtt_config.get_string("userName", "user1");
		this.mqtt_passWord=mqtt_config.get_string("passWord", "pwd");
		this.mqtt_port=mqtt_config.get_string("port", "1883");
		this.mqtt_reconnect_times=mqtt_config.get_int("mqtt_reconnect_times", 20);
		this.mqtt_disconnect_topic=mqtt_config.get_string("disconnect_topic", "as-back");
		this.mqtt_disconnect_message=mqtt_config.get_string("disconnect_message", "mcs_disconnect");
		
		String sub_mqtt_config_path=this.config_root_dir+"/mqtt_topic.properties";
		MMY_Config sub_mqtt_config=new MMY_Config(sub_mqtt_config_path);
		
		this.mqtt_sub_message_show_back=sub_mqtt_config.get_string("message-show-back", "message-show-back");
		this.mqtt_mcs_control=sub_mqtt_config.get_string("mcs_control", "mcs-control");
		this.mqtt_as_control=sub_mqtt_config.get_string("as_control", "as-control");
		this.mqtt_api_gate_control=sub_mqtt_config.get_string("api_gate_control", "api-gate-control");
		
		this.mqtt_mcs_message_show=sub_mqtt_config.get_string("mcs_message_show", "mqtt-sub-message-show-mcs");
		this.mqtt_as_message_show=sub_mqtt_config.get_string("as_message_show", "mqtt-sub-message-show-as");
		this.mqtt_api_gate_message_show=sub_mqtt_config.get_string("api_gate_message_show", "mqtt-sub-message-show-api-gate");
	}
	
}