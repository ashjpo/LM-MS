package Main.api_gate_tool;


import java.util.ArrayList;

import Main.API_GATE.model.Service;



public class service_tools{
	/**
	 * 通过service_id查询service
	 * @param service_id
	 * @return Service/null
	 */
	public static Service get_service_by_id(ArrayList<Service> service_list,String service_id) {
		for(int i=0;i<service_list.size();i++) {
			//System.out.println(service_list.get(i).service_id);
			if(service_list.get(i).service_id.equals(service_id)) {
				return service_list.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * 通过svhashid查询service
	 * @param service_list
	 * @param svhashid
	 * @return
	 */
	public static ArrayList<Service> get_service_by_svhashid(ArrayList<Service> service_list,String svhashid) {
		ArrayList<Service> return_list=new ArrayList<>();
		for(int i=0;i<service_list.size();i++) {
			if(service_list.get(i).svhashid.equals(svhashid)) {
				return_list.add(service_list.get(i));
			}
		}
		
		return return_list;
	}
	
	/**
	 * 通过service_name查询service
	 * @param service_list
	 * @param svhashid
	 * @return
	 */
	public static ArrayList<Service> get_service_by_service_name(ArrayList<Service> service_list,String service_name) {
		ArrayList<Service> return_list=new ArrayList<>();
		for(int i=0;i<service_list.size();i++) {
			if(service_list.get(i).service_name.equals(service_name)) {
				return_list.add(service_list.get(i));
			}
		}
		
		return return_list;
	}
	
	/**
	 * 通过version查询service
	 * @param service_list
	 * @param svhashid
	 * @return
	 */
	public static ArrayList<Service> get_service_by_version(ArrayList<Service> service_list,String version) {
		ArrayList<Service> return_list=new ArrayList<>();
		for(int i=0;i<service_list.size();i++) {
			if(service_list.get(i).version.equals(version)) {
				return_list.add(service_list.get(i));
			}
		}
		
		return return_list;
	}
}