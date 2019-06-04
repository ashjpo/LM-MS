package Main.API_GATE.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import Context.Main_Context;
import Main.API_GATE.model.Ask;
import Main.API_GATE.model.Service;
import Main.API_GATE.model.service_as_update_interface;
import Main.api_gate_tool.ReadFile;

/**
 * ������ȱ�
 * ע��Ҫ��ȡ�����ļ��е�����->�����ļ��Ὣ��������ۺϵ�һ��ͨ��һ��url���ã����е���/���е��ã�
 * ��urlΪ�����������Ӧ����Ӧask
 * @author mmy
 *
 */
public class service_dispatch_table{
	Main_Context mc;
	//ArrayList<match_ask> sdt_list=new ArrayList<>();
	//ArrayList<match_ask_aggregation> sdt_aggregation_list=new ArrayList<>();
	//���ڼ�¼���е���url
	//ArrayList<String> call_url_list=new ArrayList<>();
	Map<String,match_ask_aggregation> match_ask_aggregation_map=new HashMap<>();
	Map<String,match_ask> match_ask_map=new HashMap<>();
	Map<String,String> url_map=new HashMap<>();
	service_as_update_interface service_as_update_interface;
	
	Map<String,service_dealer> service_dealer_map=new HashMap<>(); 
	
	Thread flush_call_record_thread;
	Timer flush_call_record_timer=new Timer();
 	public service_dispatch_table(Main_Context mc) {
		this.mc=mc;
		
		service_as_update_interface=new service_as_update_interface() {
			
			@Override
			public void service_update() {
				// TODO Auto-generated method stub
				synchronized (this) {
					syn_service_ask();
				}
			}
		};
		this.mc.service_AS_controller.set_service_as_update(service_as_update_interface);
		
		//ˢ�������¼��
		this.start_flush_call_record();
		read_aggregation_service();
	}

	/**
	 * ��ȡ�ۺϷ���
	 */
	public void read_aggregation_service() {
		//��ʱ
		String aggregation_json_path=mc.config_obj.aggregation_path;
		String json_str=ReadFile.readToString(aggregation_json_path);
		try {
			JSONArray jsonArray=new JSONArray(json_str);
			for(int i=0;i<jsonArray.length();i++) {
				String call_url=jsonArray.getJSONObject(i).getString("call_url");
				String serial_parallel=jsonArray.getJSONObject(i).getString("serial_parallel");
				JSONArray ask=jsonArray.getJSONObject(i).getJSONArray("ask");
				JSONArray key=jsonArray.getJSONObject(i).getJSONArray("key");
				
				match_ask_aggregation match_ask_aggregation=new match_ask_aggregation();
				match_ask_aggregation.url=call_url;
				match_ask_aggregation.serial_parallel=serial_parallel;
				
				for(int j=0;j<ask.length();j++) {
					String match_call_url=ask.getString(j);
					String match_key=key.getString(j);
					match_ask_aggregation.match_ask_list.add(match_call_url);
					match_ask_aggregation.match_key_list.add(match_key);
				}
				
				if(!url_map.containsKey(call_url)) {
					url_map.put(call_url, "-aggregation-");
					match_ask_aggregation_map.put(call_url,match_ask_aggregation);
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ͬ������[TODO�Ľ�]
	 */
	private void syn_service_ask() {
		System.out.println("================================syn_service_ask >>> begin================================");
		ArrayList<Service> temp_list=this.mc.service_table.get_all_service();
		match_ask_map.clear();
		url_map.clear();
		//���servceid_list
		for (Map.Entry<String, service_dealer> entry : service_dealer_map.entrySet()) { 
			entry.getValue().serviceid_list.clear();
			entry.getValue().service_list.clear();
		}
		
		Map<String,service_dealer> temp_service_dealer_map=new HashMap<>();
		for(int i=0;i<temp_list.size();i++) {
			Service temp_service=temp_list.get(i);
			String service_id=temp_service.service_id;
			System.out.println(temp_service.service_id+" "+temp_service.service_name+" "+temp_service.version);
			ArrayList<Ask> temp_ask_list=temp_service.ask_list;
			//System.out.println("SIZE"+temp_ask_list.size());
			if(!service_dealer_map.containsKey(temp_service.service_name+temp_service.version) && !temp_service_dealer_map.containsKey(temp_service.service_name+temp_service.version)) { //first
				service_dealer service_dealer=new service_dealer(mc);
				service_dealer.service_name=temp_service.service_name;
				service_dealer.version=temp_service.version;
				service_dealer.serviceid_list.add(service_id);
				service_dealer.service_list.add(temp_service);
				temp_service_dealer_map.put(temp_service.service_name+temp_service.version, service_dealer);
				//[TODO]
			}else if(!service_dealer_map.containsKey(temp_service.service_name+temp_service.version) && temp_service_dealer_map.containsKey(temp_service.service_name+temp_service.version)) {
				service_dealer temp_service_dealer=temp_service_dealer_map.get(temp_service.service_name+temp_service.version);
				if(!temp_service_dealer.serviceid_list.contains(service_id)) {
					temp_service_dealer.serviceid_list.add(service_id);
					temp_service_dealer.service_list.add(temp_service);
				}
				//temp_service_dealer_map.put(temp_service.service_name+temp_service.version, service_dealer_map.get(temp_service.service_name+temp_service.version));
			}else if(service_dealer_map.containsKey(temp_service.service_name+temp_service.version) && temp_service_dealer_map.containsKey(temp_service.service_name+temp_service.version)) {
				service_dealer temp_service_dealer=temp_service_dealer_map.get(temp_service.service_name+temp_service.version);
				if(!temp_service_dealer.serviceid_list.contains(service_id)) {
					temp_service_dealer.serviceid_list.add(service_id);
					temp_service_dealer.service_list.add(temp_service);
				}
				
			}else {
				service_dealer temp_service_dealer=service_dealer_map.get(temp_service.service_name+temp_service.version);
				if(!temp_service_dealer.serviceid_list.contains(service_id)) {
					temp_service_dealer.serviceid_list.add(service_id);
					temp_service_dealer.service_list.add(temp_service);
				}
				temp_service_dealer_map.put(temp_service.service_name+temp_service.version, service_dealer_map.get(temp_service.service_name+temp_service.version));
			}
			for(int j=0;j<temp_ask_list.size();j++) {
				Ask temp_ask=temp_ask_list.get(j);
				String service_url="";
				if(temp_ask.call_type.equals("rpc")) {
					service_url=temp_service.service_url;
				}
				//System.out.println(temp_ask.ask_name+" "+temp_ask.http_url+" "+temp_ask_list.size());
				if(url_map.containsKey(service_url+temp_ask.http_url) && url_map.get(service_url+temp_ask.http_url).equals(temp_service.service_name+temp_service.version)) {	//ƥ�䵽֮ǰ����ͬ�ķ���
					//System.out.println("p1");
					match_ask match_ask=match_ask_map.get(service_url+temp_ask.http_url);
					match_ask.service_map.put(temp_service.service_id,temp_service);
					match_ask.ask_map.put(temp_service.service_id,temp_ask);
				}else if(url_map.containsKey(service_url+temp_ask.http_url) && !url_map.get(service_url+temp_ask.http_url).equals(temp_service.service_name+temp_service.version)){
					//pass
					//System.out.println("p2");
				}else {
					
					url_map.put(temp_service.service_id,temp_service.service_name+temp_service.version);
					//����match_ask
					match_ask match_ask=new match_ask();
					match_ask.url=service_url+temp_ask.http_url;
					match_ask.service_map.put(temp_service.service_id,temp_service);
					match_ask.ask_map.put(temp_service.service_id,temp_ask);
					url_map.put(service_url+temp_ask.http_url,temp_service.service_name+temp_service.version);
					match_ask_map.put(service_url+temp_ask.http_url,match_ask);
					//System.out.println("p3");
				}
			}
		}
		service_dealer_map=temp_service_dealer_map;
		//����ÿ��service_dealer����������¼����
		for (Map.Entry<String, service_dealer> entry : service_dealer_map.entrySet()) { 
			service_dealer_map.get(entry.getKey()).update_service_request_recorder();
		}
		System.out.println("================================syn_service_ask >>> finish================================");
	}
	
	/**
	 * ˢ��ÿ��service_dealer��service_request_record����������¼
	 */
	public void start_flush_call_record() {
		flush_call_record_thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				flush_call_record_timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						synchronized (this) {
							for (Map.Entry<String, service_dealer> entry : service_dealer_map.entrySet()) { 	//service_dealer
								Map<String,Integer> temp_map=service_dealer_map.get(entry.getKey()).service_request_record.call_num_map;
								for(Map.Entry<String, Integer> entry_call : temp_map.entrySet()) {
									temp_map.put(entry_call.getKey(),0);
								}
							}
						}
					}
				},60*1000,60*1000);
			}
		});
		flush_call_record_thread.start();
	}
	
	/**
	 * ֹͣˢ�������¼��
	 */
	public void stop_flush_call_record() {
		if(flush_call_record_thread!=null) {
			flush_call_record_thread.start();
			
		}
	}
	
	/**
	 * ��ȡ��url��ƥ���ask
	 * @param url
	 * @return
	 */
	public Object get_match_ask(String url) {
		//System.out.println(url+"==="+match_ask_map.toString()+"==="+match_ask_map.containsKey(url)+"="+match_ask_map.size());
		/*for (Map.Entry<String, match_ask> entry : match_ask_map.entrySet()) { 
			System.out.println(entry.getKey());
		}*/
		if(match_ask_map.containsKey(url)) {
			return match_ask_map.get(url);
		}
		
		if(match_ask_aggregation_map.containsKey(url)) {
			return match_ask_aggregation_map.get(url);
		}
		
		return null;
	}
	
	/**
	 * ��ȡservice_dealer
	 * @param service_name
	 * @param version
	 * @return
	 */
	public service_dealer get_service_dealer(String service_name,String version) {
		if(service_dealer_map.containsKey(service_name+version)) {
			return service_dealer_map.get(service_name+version);
		}else {
			return null;
		}
	}
	
	
}