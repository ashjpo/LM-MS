package Main.API_GATE.functions.slb;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Main.API_GATE.model.Service;
import Main.API_GATE.work.service_dealer;

public class service_load_balance{
	/**
	 * 轮询算法获取请求的service,并且记录本次请求
	 * @param service_request_record
	 * @return
	 */
	public Service slb_get_service_loop(service_request_record service_request_record) {
		if(service_request_record.last_call_serviceid.equals("")) {
			String service_id=this.get_map_key(service_request_record.service_map,0);
			if(!service_id.equals("")) {
				service_request_record.last_call_serviceid=service_id;
				if(service_request_record.call_num_map.containsKey(service_id)) {
					service_request_record.call_num_map.put(service_id,service_request_record.call_num_map.get(service_id)+1);
					if(service_request_record.service_map.containsKey(service_id)) {
						return service_request_record.service_map.get(service_id);
					}else {
						return null;
					}
				}else {
					service_request_record.call_num_map.put(service_id,0);
					if(service_request_record.service_map.containsKey(service_id)) {
						return service_request_record.service_map.get(service_id);
					}else {
						return null;
					}
				}
			}else {
				return null;
			}
		}else {
			String last_serviceid=service_request_record.last_call_serviceid;
			if(service_request_record.service_map.containsKey(last_serviceid)) {
				int key_index=this.get_map_indexof(service_request_record.service_map,last_serviceid)+1;
				//System.out.println(key_index+"="+service_request_record.service_map.size());
				if(key_index==(service_request_record.service_map.size())) {
					key_index=0;
				}
				String service_id=this.get_map_key(service_request_record.service_map,key_index);
				if(!service_id.equals("")) {
					service_request_record.last_call_serviceid=service_id;
					if(service_request_record.call_num_map.containsKey(service_id)) {
						service_request_record.call_num_map.put(service_id,service_request_record.call_num_map.get(service_id)+1);
						if(service_request_record.service_map.containsKey(service_id)) {
							return service_request_record.service_map.get(service_id);
						}else {
							return null;
						}
					}else {
						service_request_record.call_num_map.put(service_id,0);
						if(service_request_record.service_map.containsKey(service_id)) {
							return service_request_record.service_map.get(service_id);
						}else {
							return null;
						}
					}
				}else {
					return null;
				}
			}else {
				String service_id=this.get_map_key(service_request_record.service_map,0);
				if(!service_id.equals("")) {
					service_request_record.last_call_serviceid=service_id;
					if(service_request_record.call_num_map.containsKey(service_id)) {
						service_request_record.call_num_map.put(service_id,service_request_record.call_num_map.get(service_id)+1);
						if(service_request_record.service_map.containsKey(service_id)) {
							return service_request_record.service_map.get(service_id);
						}else {
							return null;
						}
					}else {
						service_request_record.call_num_map.put(service_id,0);
						if(service_request_record.service_map.containsKey(service_id)) {
							return service_request_record.service_map.get(service_id);
						}else {
							return null;
						}
					}
				}else {
					return null;
				}
			}
		}
	}
	
	/**
	 * 随机算法获取请求的service,并且记录本次请求
	 * @param service_request_record
	 * @return
	 */
	public Service slb_get_service_random(service_request_record service_request_record) {
		int all_service_num=service_request_record.service_map.size();
		Random ra =new Random();
		int index=ra.nextInt(all_service_num)-1;
		String service_id=this.get_map_key(service_request_record.service_map,index);
		if(!service_id.equals("")) {
			service_request_record.last_call_serviceid=service_id;
			if(service_request_record.call_num_map.containsKey(service_id)) {
				service_request_record.call_num_map.put(service_id,service_request_record.call_num_map.get(service_id)+1);
				if(service_request_record.service_map.containsKey(service_id)) {
					return service_request_record.service_map.get(service_id);
				}else {
					return null;
				}
			}else {
				service_request_record.call_num_map.put(service_id,0);
				if(service_request_record.service_map.containsKey(service_id)) {
					return service_request_record.service_map.get(service_id);
				}else {
					return null;
				}
			}
		}else {
			return null;
		}
	}
	
	/**
	 * 加权轮询算法获取请求的service,并且记录本次请求
	 * @param service_request_record
	 * @return
	 */
	public Service slb_get_service_weight_loop(service_request_record service_request_record) {
		/*int all_service_request_times=0;
		for (Map.Entry<String,Integer> entry : service_request_record.call_num_map.entrySet()) {
			all_service_request_times=all_service_request_times+entry.getValue();
		}
		
		//生成临时列表
		Map<String,Integer> temp_list=new HashMap<>();
		for (Map.Entry<String,Integer> entry : service_request_record.call_num_map.entrySet()) {
			
		}*/
		return null;
	}
	
	public String get_map_key(Map<String,?> map,int i) {
		int index=0;
		for (Map.Entry<String, ?> entry : map.entrySet()) { 
			if(index==i) {
				return entry.getKey();
			}
			index++;
		}
		return "";
	}
	
	public int get_map_indexof(Map<String,?> map,String key) {
		int index=0;
		for (Map.Entry<String, ?> entry : map.entrySet()) { 
			if(key.equals(entry.getKey())) {
				return index;
			}
			index++;
		}
		return -1;
	}
}