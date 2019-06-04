package Main.API_GATE.functions.cache;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import Context.Main_Context;
import Main.API_GATE.model.Ask;
import Main.API_GATE.model.Service;
import Main.API_GATE.work.match_ask;
import redis.clients.jedis.Jedis;

/**
 * 将内容缓存到内存中
 * @author mmy
 *
 */
public class memory_cache{
	//超出这个时间缓存将被删除
	long cache_limit_time=60;	//s
	private Jedis jedis;
	public memory_cache(Main_Context mc) {
		cache_limit_time=mc.config_obj.memory_cache_limit_time;
		jedis = new Jedis(mc.config_obj.redis_host,mc.config_obj.redis_port);
		clear_cache();
		
	}
	
	public void clear_cache() {
		jedis.flushDB();
	}
	
	/**
	 * 获取文件缓存
	 * @param service
	 * @return
	 */
	public String get_cache(match_ask mAsk) {
		Service service=this.getMapFirstService(mAsk.service_map);
		String service_name=service.service_name;
		String version=service.version;
		Ask ask=this.getMapFirstAsk(mAsk.ask_map);
		String ask_name=ask.ask_name;
		long current_time=(long)System.currentTimeMillis()/1000;
		String cache_key=service_name+"_"+version+"_"+ask_name;
		String cache_value = jedis.get(cache_key);
		if(cache_value==null) {
			return "";   
		}else {
			return cache_value;
		}
             
		
	}
	
	/**
	 * 设置缓存 返回是否设置成功 force=false如果之前缓存没有到时就不设置 force=true更新
	 * @param service
	 * @param message
	 */
	public boolean set_cache(match_ask mAsk,String message,Boolean force) {
		Service service=this.getMapFirstService(mAsk.service_map);
		String service_name=service.service_name;
		String version=service.version;
		Ask ask=this.getMapFirstAsk(mAsk.ask_map);
		String ask_name=ask.ask_name;
		long current_time=(long)System.currentTimeMillis()/1000;
		String cache_key=service_name+"_"+version+"_"+ask_name;
		String cache_value = jedis.get(cache_key);
		if(cache_value==null) {
			jedis.set(cache_key,cache_value, "NX", "EX", this.cache_limit_time);
		}else {
			if(force) {
				jedis.del(cache_key);
				jedis.set(cache_key,cache_value, "NX", "EX", this.cache_limit_time);
			}
		}
		
		return true;
	}
	
	private Service getMapFirstService(Map<String, Service> ask_map) {    	
		Service obj = null;        
		for (Entry<String, Service> entry : ask_map.entrySet()) {            
			obj = entry.getValue();            
			if (obj != null) {                
				break;            
			}        
		}        
		return  obj;    
	}
	
	private Ask getMapFirstAsk(Map<String, Ask> ask_map) {    	
		Ask obj = null;        
		for (Entry<String, Ask> entry : ask_map.entrySet()) {            
			obj = entry.getValue();            
			if (obj != null) {                
				break;            
			}        
		}        
		return  obj;    
	}
}