package Main.API_GATE.functions.wblist;

import java.util.ArrayList;

import Context.Main_Context;
import Main.api_gate_tool.ReadFile;
import Tools.MMY_Config;

public class iplist{
	private boolean if_use=true;
	ArrayList<String> wList=new ArrayList<>();
	ArrayList<String> bList=new ArrayList<>();
	Main_Context mc;
	
	public iplist(Main_Context mc) {
		this.mc=mc;
		//临时
		MMY_Config my_config=new MMY_Config(mc.config_obj.iplist_config_path);
		
		if_use=my_config.get_boolean("if_use", false);
		
		String wlist=my_config.get_string("wlist", "");
		String blist=my_config.get_string("blist", "");
		if(!wlist.equals("")) {
			String[] wlist_arr=wlist.split("\\|");
			for(int i=0;i<wlist_arr.length;i++) {
				wList.add(wlist_arr[i]);
			}
		}
		
		if(!blist.equals("")) {
			String[] blist_arr=blist.split("\\|");
			for(int i=0;i<blist_arr.length;i++) {
				bList.add(blist_arr[i]);
			}
		}
	}
	
	
	
	
	/**
	 * 设置是否使用ip名单
	 * @param if_use
	 */
	public void set_if_use(boolean if_use) {
		this.if_use=if_use;
	}
	
	/**
	 * 判断是否可以通过ip名单
	 * @param ip
	 * @return
	 */
	public boolean if_can_pass(String ip) {
		if(if_use) {
			if(wList.size()==0) {
				if(bList.contains(ip)) {
					
					return false;
				}else {
					return true;
				}
			}else {
				if(wList.contains(ip)) {
					
					return true;
				}else {
					
					return false;
				}
			}
		}else {
			return true;
		}
	}
}