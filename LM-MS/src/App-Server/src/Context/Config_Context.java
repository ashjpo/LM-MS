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
	
	public String mqtt_sub_as_control=null;
	public String mqtt_sub_message_show=null;
	public String mqtt_logout=null;
	//发送topic
	public String as_back=null;
	public String message_show_back=null;
	
	public String service_php_init_file_path=null;
	public String service_python_init_file_path=null;
	public String service_java_init_file_path=null;
	
	public int as_hart_time;
		
	public Config_Context(String config_root_dir) {
		this.config_root_dir=config_root_dir;
		this.init_main_config();
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
		
		this.mqtt_sub_as_control=sub_mqtt_config.get_string("mqtt_sub_as_control", "as-control");
		this.mqtt_sub_message_show=sub_mqtt_config.get_string("mqtt_sub_message_show", "mqtt-sub-message-show-as");
		this.mqtt_logout=sub_mqtt_config.get_string("mqtt_logout", "logout");
		
		//发送topic
		this.as_back=sub_mqtt_config.get_string("as_back", "as-back");
		this.message_show_back=sub_mqtt_config.get_string("message_show_back", "message-show-back");
	}
	
	private void init_main_config() {
		String mqtt_config_path=this.config_root_dir+"/main.properties";
		MMY_Config mqtt_config=new MMY_Config(mqtt_config_path);
		
		this.service_php_init_file_path=mqtt_config.get_string("service_php_init_file_path", "");
		this.service_python_init_file_path=mqtt_config.get_string("service_python_init_file_path", "");
		this.service_java_init_file_path=mqtt_config.get_string("service_java_init_file_path", "");
		
		this.as_hart_time=mqtt_config.get_int("as_hart_time", 60);
	}
	
	
}