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
	
	public String mqtt_sub_api_gate_control=null;
	public String mqtt_sub_message_show=null;
	public String mqtt_logout=null;
	//发送topic
	public String api_gate_back=null;
	public String message_show_back=null;
	
	public String service_php_init_file_path=null;
	public String service_python_init_file_path=null;
	public String service_java_init_file_path=null;
	
	public int api_gate_hart_time;
	
	public String server_port=null;
	
	public String text_cache_dir_path=null;
	public String html_cache_dir_path=null;
	public int text_cache_limit_time;
	public int recover_midopen_time;
	public boolean cache_if_use;
	
	public int flush_time;
	public int max_request_times;
	public boolean ddos_if_use;
	
	public int limit_fusing_error_times;
	public int recover_open_times;
	public int html_cache_limit_time;
	public int memory_cache_limit_time;
	public boolean fusing_if_use;
	
	public String redis_host;
	public int redis_port;
	
	public String aggregation_path;
	
	public int api_gate_average_flow;
	public int api_gate_max_flow;
	public int service_default_average_flow;
	public int service_default_max_flow;
	public int service_default_bucket_size;
	
	public String iplist_config_path;
	
		
	public Config_Context(String config_root_dir) {
		this.config_root_dir=config_root_dir;
		this.init_main_config();
		//初始化mqtt配置
		this.init_mqtt_config();		
		//服务器相关
		this.init_server_config();
		//cache
		this.init_cache_config();
		//ddos
		this.init_ddos_config();
		//熔断
		this.init_fusing_config();
		//流量控制
		this.flow_control_config();
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
		this.mqtt_disconnect_topic=mqtt_config.get_string("disconnect_topic", "api-gate-back");
		this.mqtt_disconnect_message=mqtt_config.get_string("disconnect_message", "mcs_disconnect");
		
		String sub_mqtt_config_path=this.config_root_dir+"/mqtt_topic.properties";
		MMY_Config sub_mqtt_config=new MMY_Config(sub_mqtt_config_path);
		
		this.mqtt_sub_api_gate_control=sub_mqtt_config.get_string("mqtt_sub_api_gate_control", "api-gate-control");
		this.mqtt_sub_message_show=sub_mqtt_config.get_string("mqtt_sub_message_show", "mqtt-sub-message-show-api-gate");
		this.mqtt_logout=sub_mqtt_config.get_string("mqtt_logout", "logout");
		
		//发送topic
		this.api_gate_back=sub_mqtt_config.get_string("api_gate_back", "api-gate-back");
		this.message_show_back=sub_mqtt_config.get_string("message_show_back", "message-show-back");
		
		this.iplist_config_path=this.config_root_dir+"/iplist.properties";
	}
	
	private void init_main_config() {
		String mqtt_config_path=this.config_root_dir+"/main.properties";
		MMY_Config mqtt_config=new MMY_Config(mqtt_config_path);

		this.api_gate_hart_time=mqtt_config.get_int("api_gate_hart_time", 60);
		
		this.aggregation_path=mqtt_config.get_string("aggregation_path", "D:/Api_Gate/aggregation/aggregation_service.json");
	}
	
	private void init_server_config() {
		String mqtt_config_path=this.config_root_dir+"/server.properties";
		MMY_Config mqtt_config=new MMY_Config(mqtt_config_path);

		this.server_port=mqtt_config.get_string("port", "8084");
	}
	
	private void init_cache_config() {
		String mqtt_config_path=this.config_root_dir+"/cache.properties";
		MMY_Config mqtt_config=new MMY_Config(mqtt_config_path);

		this.text_cache_dir_path=mqtt_config.get_string("text_cache_dir_path", "CacheTextTest");
		this.html_cache_dir_path=mqtt_config.get_string("html_cache_dir_path", "CacheHtmlTest");
		this.text_cache_limit_time=mqtt_config.get_int("text_cache_limit_time", 60);
		this.html_cache_limit_time=mqtt_config.get_int("html_cache_limit_time", 60);
		this.memory_cache_limit_time=mqtt_config.get_int("memory_cache_limit_time", 60);
		this.redis_host=mqtt_config.get_string("redis_host", "127.0.0.1");
		this.redis_port=mqtt_config.get_int("redis_port", 6379);
		this.cache_if_use=mqtt_config.get_boolean("if_use", false);
	}
	
	private void init_ddos_config() {
		String mqtt_config_path=this.config_root_dir+"/ddos.properties";
		MMY_Config mqtt_config=new MMY_Config(mqtt_config_path);

		this.flush_time=mqtt_config.get_int("flush_time", 30);
		this.max_request_times=mqtt_config.get_int("max_request_times", 60);
		this.ddos_if_use=mqtt_config.get_boolean("if_use", false);
	}
	
	private void init_fusing_config() {
		String mqtt_config_path=this.config_root_dir+"/fusing.properties";
		MMY_Config mqtt_config=new MMY_Config(mqtt_config_path);

		this.limit_fusing_error_times=mqtt_config.get_int("limit_fusing_error_times", 100);
		this.recover_open_times=mqtt_config.get_int("recover_open_times", 5);
		this.recover_midopen_time=mqtt_config.get_int("recover_midopen_time", 60);
		this.fusing_if_use=mqtt_config.get_boolean("if_use", false);
	}
	
	private void flow_control_config() {
		String mqtt_config_path=this.config_root_dir+"/fusing.properties";
		MMY_Config mqtt_config=new MMY_Config(mqtt_config_path);

		this.api_gate_average_flow=mqtt_config.get_int("api-gate-average-flow", 51200);
		this.api_gate_max_flow=mqtt_config.get_int("api-gate-max-flow", 102400);
		this.service_default_average_flow=mqtt_config.get_int("service-default-average-flow", 51200);
		this.service_default_max_flow=mqtt_config.get_int("service-default-max-flow", 102400);
		this.service_default_bucket_size=mqtt_config.get_int("bucket_size", 10240000);
	}
	
	
}