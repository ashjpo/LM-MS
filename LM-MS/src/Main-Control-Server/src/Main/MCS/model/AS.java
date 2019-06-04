package Main.MCS.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Main.mcs_tool.service_tools;

public class AS{
	public String node_id;
	public String as_mes="";
	public ArrayList<Service> service_list=new ArrayList<>();
	public long start_time;
	//心跳包时间
	public long last_hart_time;
	public AS(String node_id) {
		this.node_id=node_id;
		this.start_time=System.currentTimeMillis();
		this.last_hart_time=System.currentTimeMillis();
	}
	
	/**
	 * 添加service
	 * @param service
	 */
	public void add_service(Service service) {
		if(service_tools.get_service_by_id(this.service_list, service.service_id)==null) {
			this.service_list.add(service);
		}
	}
	
	/**
	 * 删除service
	 * @param service
	 */
	public void delete_service(Service service) {
		if(service_tools.get_service_by_id(this.service_list, service.service_id)!=null) {
			this.service_list.remove(service);
		}
	}
	
	public void delete_service_list(ArrayList<Service> del_list) {
		this.service_list.removeAll(del_list);
	}
	
	public ArrayList<Service> get_all_service_by_id(String service_id) {
		ArrayList<Service> return_list=new ArrayList<>();
		for(int i=0;i<service_list.size();i++) {
			if(service_list.get(i).service_id.equals(service_id)) {
				return_list.add(service_list.get(i));
			}
		}
		return return_list;
	}
	
	/**
	 * 获取所有服务对象
	 * @return
	 */
	public ArrayList<Service> get_all_service() {
		return this.service_list;
	}
	
	public ArrayList<Service> get_all_service_by_svhashid(String svhashid) {
		ArrayList<Service> return_list=new ArrayList<>();
		for(int j=0;j<service_list.size();j++) {
			if(service_list.get(j).svhashid.equals(svhashid)) {
				return_list.add(service_list.get(j));
			}
		}
		
		return return_list;
	}
	
	/**
	 * 获取所有服务对象svhashid
	 * @return
	 */
	public ArrayList<Service> get_service_name_version(String service,String version) {
		if(service.equals("*") && version.equals("*")) {
			return service_list;
		}else if(service.equals("*") && !version.equals("*")) {
			ArrayList<Service> return_list=new ArrayList<>();
			String[] v_arr=version.split("\\|");
			for(int j=0;j<service_list.size();j++) {
				if(Arrays.asList(v_arr).contains(service_list.get(j).version)) {
					return_list.add(service_list.get(j));
				}
			}
			return return_list;
		}else if(!service.equals("*") && version.equals("*")) {
			ArrayList<Service> return_list=new ArrayList<>();
			String[] s_arr=service.split("\\|");
			for(int j=0;j<service_list.size();j++) {
				if(Arrays.asList(s_arr).contains(service_list.get(j).service_name)) {
					return_list.add(service_list.get(j));
				}
			}
			return return_list;
		}else {
			ArrayList<Service> return_list=new ArrayList<>();
			ArrayList<Service> mid_list=new ArrayList<>();
			String[] s_arr=service.split("\\|");
			String[] v_arr=version.split("\\|");
			for(int j=0;j<service_list.size();j++) {
				if(Arrays.asList(s_arr).contains(service_list.get(j).service_name)) {
					mid_list.add(service_list.get(j));
				}
			}
			
			for(int j=0;j<mid_list.size();j++) {
				if(Arrays.asList(v_arr).contains(mid_list.get(j).version)) {
					return_list.add(mid_list.get(j));
				}
			}
			
			return return_list;
		}
		
	}
	
	
	
	/**
	 * 获取所有服务对象service_name
	 * @return
	 */
	public ArrayList<Service> get_all_service_by_service_name(String service_name) {
		return service_tools.get_service_by_service_name(service_list, service_name);
	}
	
	/**
	 * 获取所有服务对象version
	 * @return
	 */
	public ArrayList<Service> get_all_service_by_version(String version) {
		return service_tools.get_service_by_version(service_list, version);
	}
	
	
}