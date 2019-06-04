package Main.API_GATE.functions.cache;

import Context.Main_Context;
import Main.API_GATE.model.Service;
import Main.API_GATE.service.HttpContext;
import Main.API_GATE.work.match_ask;

public class cache_main{
	Main_Context mc;
	text_cache text_cache_obj;
	html_cache html_cache_obj;
	memory_cache memory_cache_obj;
	public cache_main(Main_Context mc) {
		this.mc=mc;
		text_cache_obj=new text_cache(mc);
		html_cache_obj=new html_cache(mc);
		memory_cache_obj=new memory_cache(mc);
		if_use=mc.config_obj.cache_if_use;
	}
	public boolean if_use=false;
	
	/**
	 * 设置是否使用
	 * @param if_use
	 */
	public void set_if_use(boolean if_use) {
		this.if_use=if_use;
	}
	
	/**
	 * 获取缓存 返回缓存对象或者null
	 * @param cache_type
	 * @param mAsk
	 * @return
	 */
	public String get_cache(String cache_type,match_ask mAsk) {
		System.out.println("CACHE>>>"+if_use);
		if(!if_use) {
			return null;
		}
		if(cache_type.equals("text")) {
			return text_cache_obj.get_cache(mAsk);
		}else if(cache_type.equals("memory")) {
			return memory_cache_obj.get_cache(mAsk);
		}else if(cache_type.equals("html")) {
			return html_cache_obj.get_cache(mAsk);
		}
		//临时
		return null;
	}
	
	public void set_cache(String message,String cache_type,match_ask mAsk,boolean force) {
		if(!if_use) {
			
		}else {
			if(cache_type.equals("text")) {
				text_cache_obj.set_cache(mAsk, message, force);
			}else if(cache_type.equals("memory")) {
				memory_cache_obj.set_cache(mAsk, message, force);
			}else if(cache_type.equals("html")) {
				html_cache_obj.set_cache(mAsk, message, force);
			}
		}
		
	}
}