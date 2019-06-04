package Main.API_GATE.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import Context.Main_Context;
import Main.api_gate_tool.service_tools;

public class Service_table{
	public ArrayList<Service> service_list=new ArrayList<>();
	Main_Context mc;
	public Service_table(Main_Context mc) {
		this.mc=mc;
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
	
	/**
	 * 删除某个as上的所有服务
	 * @param as
	 */
	public void delete_service_as(AS as) {
		ArrayList<Service> this_as_service=as.get_all_service();
		Iterator<Service> it = this_as_service.iterator();
		while(it.hasNext()){
			Service temp_service = it.next();
			String this_service_id=temp_service.service_id;
			Service temp_s=service_tools.get_service_by_id(service_list,this_service_id);
			delete_service(temp_s);
		    it.remove();
		    
		}
	}
	
	/**
	 * 获取service_model
	 * @param service_name
	 * @param version
	 * @return
	 */
	public ArrayList<Service> get_service(String service_name_l,String version_l) {
		ArrayList<Service> return_list=new ArrayList<>();
		if(service_name_l.equals("*") && version_l.equals("*")) {	//所有
			return_list=service_list;
			return return_list;
		}else if(service_name_l.equals("*") && !version_l.equals("*")) {
			String[] versionarr=version_l.split("\\|");
			for(int i=0;i<versionarr.length;i++) {
				String version=versionarr[i];
				ArrayList<Service> temp_list=this.get_all_service_by_version(version);
				return_list.addAll(temp_list);
			}
			return return_list;
		}else if(!service_name_l.equals("*") && version_l.equals("*")) {
			String[] service_namearr=service_name_l.split("\\|");
			for(int i=0;i<service_namearr.length;i++) {
				String service_name=service_namearr[i];
				ArrayList<Service> temp_list=this.get_all_service_by_service_name(service_name);
				return_list.addAll(temp_list);
			}
			return return_list;
		}else {
			ArrayList<Service> mid_list=new ArrayList<>();
			String[] service_namearr=service_name_l.split("\\|");
			String[] versionarr=version_l.split("\\|");
			for(int i=0;i<service_namearr.length;i++) {
				String service_name=service_namearr[i];
				ArrayList<Service> temp_list=this.get_all_service_by_service_name(service_name);
				mid_list.addAll(temp_list);
			}
			
			for(int i=0;i<mid_list.size();i++) {
				if(Arrays.asList(versionarr).contains(mid_list.get(i).version)) {
					return_list.add(mid_list.get(i));
				}
			}
			return return_list;
		}
		
	}
	
	/**
	 * 获取所有服务对象
	 * @return
	 */
	public ArrayList<Service> get_all_service() {
		return this.service_list;
	}
	

	
	/**
	 * 获取所有服务对象svhashid
	 * @return
	 */
	public ArrayList<Service> get_all_service_by_svhashid(String svhashid) {
		return service_tools.get_service_by_svhashid(service_list, svhashid);
	}
	
	/**
	 * 获取所有服务对象svhashid
	 * @return
	 */
	public Service get_all_service_by_id(String service_id) {
		Service temp=service_tools.get_service_by_id(service_list, service_id);
		return temp;
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