package Main.API_GATE.functions.service_reduce;

import Context.Main_Context;
import Main.API_GATE.work.match_ask;

/**
 * 服务降级[TODO]
 * @author mmy
 *
 */
public class service_reduce_main{
	Main_Context mc;
	public service_reduce_main(Main_Context mc) {
		this.mc=mc;
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
	 * 是否服务降级
	 * @param mAsk
	 * @return
	 */
	public boolean if_reduce_service(match_ask mAsk) {
		
		//临时
		return false;
	}
}