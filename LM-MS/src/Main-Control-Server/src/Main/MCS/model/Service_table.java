package Main.MCS.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import Context.Main_Context;
import Main.mcs_tool.service_tools;
public class Service_table{
	public ArrayList<Service> service_list=new ArrayList<>();
	Main_Context mc;
	public Service_table(Main_Context mc) {
		this.mc=mc;
	}
	/**
	 * ���service
	 * @param service
	 */
	public void add_service(Service service) {
		if(service_tools.get_service_by_id(this.service_list, service.service_id)==null) {
			this.service_list.add(service);
		}
	}
	
	/**
	 * ɾ��service
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
	 * ɾ��ĳ��as�ϵ����з���
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
	 * ��ȡ���з������
	 * @return
	 */
	public ArrayList<Service> get_all_service() {
		return this.service_list;
	}
	

	
	/**
	 * ��ȡ���з������svhashid
	 * @return
	 */
	public ArrayList<Service> get_all_service_by_svhashid(String svhashid) {
		return service_tools.get_service_by_svhashid(service_list, svhashid);
	}
	
	/**
	 * ��ȡ���з������svhashid
	 * @return
	 */
	public Service get_all_service_by_id(String service_id) {
		return service_tools.get_service_by_id(service_list, service_id);
	}
	
	/**
	 * ��ȡ���з������service_name
	 * @return
	 */
	public ArrayList<Service> get_all_service_by_service_name(String service_name) {
		return service_tools.get_service_by_service_name(service_list, service_name);
	}
	
	/**
	 * ��ȡ���з������version
	 * @return
	 */
	public ArrayList<Service> get_all_service_by_version(String version) {
		return service_tools.get_service_by_version(service_list, version);
	}
}