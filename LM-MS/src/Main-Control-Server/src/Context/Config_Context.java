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
	//mcs-control,as-back,api-gate-back
	public String mqtt_sub_mcs_control=null;
	public String mqtt_sub_as_back=null;
	public String mqtt_sub_api_gate_back=null;
	public String mqtt_sub_message_show=null;
	public String mqtt_logout=null;
	//发送topic
	public String mqtt_as_control=null;
	public String mqtt_api_gate_control=null;
	public String message_show_back=null;
	
	//as
	public int as_hart_time=120;
	public int as_hart_outline_time=300;
	public int as_monitor_hart_time=60;
	
	//service
	public int service_hart_time=120;
	public int service_hart_outline_time=300;
	
	//api-gate
	public int api_gate_hart_time=120;
	public int api_gate_hart_outline_time=120;
	public int api_gate_monitor_hart_time=300;
	
	
		
	public Config_Context(String config_root_dir) {
		this.config_root_dir=config_root_dir;
		//初始化mqtt配置
		this.init_mqtt_config();
		//初始化AS,api-gate配置
		this.init_as_config();
		this.init_api_gate_config();
		
		
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
		this.mqtt_disconnect_topic=mqtt_config.get_string("disconnect_topic", "mcs_disconnect_topic");
		this.mqtt_disconnect_message=mqtt_config.get_string("disconnect_message", "mcs_disconnect");
		
		String sub_mqtt_config_path=this.config_root_dir+"/mqtt_topic.properties";
		MMY_Config sub_mqtt_config=new MMY_Config(sub_mqtt_config_path);
		
		this.mqtt_sub_mcs_control=sub_mqtt_config.get_string("mqtt_sub_mcs_control", "mcs-control");
		this.mqtt_sub_as_back=sub_mqtt_config.get_string("mqtt_sub_as_back", "as-back");
		this.mqtt_sub_api_gate_back=sub_mqtt_config.get_string("mqtt_sub_api_gate_back", "api-gate-back");
		this.mqtt_sub_message_show=sub_mqtt_config.get_string("mqtt_sub_message_show", "mqtt-sub-message-show-mcs");
		this.mqtt_logout=sub_mqtt_config.get_string("mqtt_logout", "logout");
		
		//发送topic
		this.mqtt_as_control=sub_mqtt_config.get_string("as_control", "as-control");
		this.mqtt_api_gate_control=sub_mqtt_config.get_string("api_gate_control", "api-gate-control");
		this.message_show_back=sub_mqtt_config.get_string("message_show_back", "message-show-back");
	}
	
	/**
	 * 初始化AS配置
	 */
	private void init_as_config() {
		String service_config_path=this.config_root_dir+"/as.properties";
		MMY_Config mqtt_config=new MMY_Config(service_config_path);
		
		this.as_hart_time=mqtt_config.get_int("as-hart-time", 120);
		this.as_hart_outline_time=mqtt_config.get_int("as-hart-outline-time", 300);
		this.as_monitor_hart_time=mqtt_config.get_int("as-monitor-hart-timee", 60);
		
		this.service_hart_time=mqtt_config.get_int("service-hart-time", 120);
		this.service_hart_outline_time=mqtt_config.get_int("service-hart-outline-time", 300);
		
		
	}
	
	/**
	 * 初始化api-gate配置
	 */
	private void init_api_gate_config() {
		String service_config_path=this.config_root_dir+"/api_gate.properties";
		MMY_Config mqtt_config=new MMY_Config(service_config_path);
		
		this.api_gate_hart_time=mqtt_config.get_int("api-gate-hart-time", 120);
		this.api_gate_hart_outline_time=mqtt_config.get_int("api-gate-hart-outline-time", 300);
		this.api_gate_monitor_hart_time=mqtt_config.get_int("api-gate-monitor-hart-time", 60);

		
		
	}
}