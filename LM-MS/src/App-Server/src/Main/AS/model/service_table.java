package Main.AS.model;

import java.util.ArrayList;
import java.util.Arrays;

public class service_table{
	ArrayList<service> s_list=new ArrayList<>();
	public int begin_service_port=5000;
	public ArrayList<Integer> port_list=new ArrayList<>();
	public void set_service_port_list(int port) {
		synchronized (this) {
			port_list.add(port);
		}
	}
	
	public ArrayList<Integer> get_service_port_list() {
		return this.port_list;
	}
	/**
	 * 添加service_model
	 * @param service_model
	 */
	public void add_service(service service) {
		s_list.add(service);
	}
	
	public void delete_service(service service) {
		s_list.remove(service);
	}
	
	public void delete_service_list(ArrayList<service> del_list) {
		s_list.removeAll(del_list);
	}
	
	/**
	 * 通过service_id获取service
	 * @param service_id
	 * @return
	 */
	public service get_service_by_id(String service_id) {
		for(int i=0;i<s_list.size();i++) {
			if(s_list.get(i).service_id.equals(service_id)) {
				return s_list.get(i);
			}
		}
		return null;
	}
	
	/**
	 * 获取service_model
	 * @param service_name
	 * @param version
	 * @return
	 */
	public ArrayList<service> get_service(String service_name_l,String version_l) {
		ArrayList<service> return_list=new ArrayList<>();
		if(service_name_l.equals("*") && version_l.equals("*")) {	//所有
			return_list=s_list;
			return return_list;
		}else if(service_name_l.equals("*") && !version_l.equals("*")) {
			String[] versionarr=version_l.split("\\|");
			for(int i=0;i<versionarr.length;i++) {
				String version=versionarr[i];
				ArrayList<service> temp_list=this.get_service_by_version(version);
				return_list.addAll(temp_list);
			}
			return return_list;
		}else if(!service_name_l.equals("*") && version_l.equals("*")) {
			String[] service_namearr=service_name_l.split("\\|");
			for(int i=0;i<service_namearr.length;i++) {
				String service_name=service_namearr[i];
				ArrayList<service> temp_list=this.get_service_by_name(service_name);
				return_list.addAll(temp_list);
			}
			return return_list;
		}else {
			ArrayList<service> mid_list=new ArrayList<>();
			String[] service_namearr=service_name_l.split("\\|");
			String[] versionarr=version_l.split("\\|");
			for(int i=0;i<service_namearr.length;i++) {
				String service_name=service_namearr[i];
				ArrayList<service> temp_list=this.get_service_by_name(service_name);
				mid_list.addAll(temp_list);
			}
			
			for(int i=0;i<mid_list.size();i++) {
				if(Arrays.asList(versionarr).contains(mid_list.get(i).service_model_obj.version)) {
					return_list.add(mid_list.get(i));
				}
			}
			return return_list;
		}
		
	}
	
	/**
	 * 获取某个version的服务【不需要直接调用】
	 * @param version
	 * @return
	 */
	private ArrayList<service> get_service_by_version(String version) {
		ArrayList<service> return_list=new ArrayList<>();
		for(int i=0;i<s_list.size();i++) {
			if(s_list.get(i).service_model_obj.version.equals(version)) {
				return_list.add(s_list.get(i));
			}
		}
		
		return return_list;
	}
	
	/**
	 * 获取某个name的服务【不需要直接调用】
	 * @param name
	 * @return
	 */
	private ArrayList<service> get_service_by_name(String name) {
		ArrayList<service> return_list=new ArrayList<>();
		for(int i=0;i<s_list.size();i++) {
			if(s_list.get(i).service_model_obj.service_name.equals(name)) {
				return_list.add(s_list.get(i));
			}
		}
		
		return return_list;
	}
}